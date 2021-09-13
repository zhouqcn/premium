package com.finmind.premium;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class Quote {
    final private int MAX_NUM_RESULTS = 3;
    private HashMap<InsurerName, InsurerInfo> insurerInfoMap = new HashMap<>();

    final private List<Long> COVERAGE_AMOUNT_LIST = Arrays.asList(150000L, 200000L,	250000L, 300000L, 350000L, 400000L,	450000L, 500000L, 600000L, 700000L, 750000L, 800000L, 900000L, 1000000L, 1100000L, 1200000L, 1250000L, 1500000L, 1750000L, 2000000L, 2500000L, 2750000L, 3000000L, 3500000L, 4000000L, 4500000L, 5000000L, 6000000L, 7000000L, 8000000L, 9000000L, 10000000L, 11000000L, 12000000L, 12500000L, 15000000L, 20000000L);

    public Quote() {
    }

    public void loadDataFromConfig(Config config) throws Exception {
        Map<String, InsurerConfig> insurerConfigs = config.getInsurerConfigs();

        for (Map.Entry<String, InsurerConfig> entry : insurerConfigs.entrySet()) {
            InsurerConfig c = entry.getValue();
            InsurerInfo insurer = new InsurerInfo(c);

            InsurerName insurerName = InsurerName.getInsurerNameFromStringOrNull(entry.getKey());
            if (insurerName == null) {
                throw new IllegalArgumentException("Insurer name is invalid in config.");
            }

            insurerInfoMap.put(insurerName, insurer);
        }
    }

    private int processAge(Date birthday) {
        Date current = Date.from(Instant.now());
        DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        int d1 = Integer.parseInt(formatter.format(birthday));
        int d2 = Integer.parseInt(formatter.format(current));
        int age = (d2 - d1)/10000;
        return age;
    }

    private PremiumClass processPremiumClass(QuoteRequest request) {
        if (request.isMilitaryService() ||
            request.isHighRiskActivity()) {
            return PremiumClass.HIGH_RISK;
        }

        if (request.getSmoke() == LivingStyleAnswer.OFTEN ||
            request.getMarijuana() == LivingStyleAnswer.SOMETIMES ||
            request.getMarijuana() == LivingStyleAnswer.OFTEN ||
            request.getDrug() != LivingStyleAnswer.NEVER) {
            return PremiumClass.HIGH_RISK;
        }

        return PremiumClass.STANDARD;
    }

    private List<QuoteResult> getQuoteResults(Gender gender, int age, PremiumClass premiumClass, int coverageTime, Long coverageAmount) {
        List<QuoteResult> results = new ArrayList<>();
        for (Map.Entry<InsurerName, InsurerInfo> entry : insurerInfoMap.entrySet()) {
            InsurerName insurerName = entry.getKey();
            InsurerInfo insurerInfo = entry.getValue();
            PremiumInfo premiumInfo = insurerInfo.getPremiumInfo(gender, coverageTime, premiumClass);
            if (premiumInfo == null && premiumClass != PremiumClass.STANDARD) {
                premiumInfo = insurerInfo.getPremiumInfo(gender, coverageTime, PremiumClass.STANDARD);
            }

            if (premiumInfo == null) {
                continue;
            }

            Float premium = premiumInfo.getPremium(age, coverageAmount);
            if (premium == null) {
                continue;
            }

            results.add(new QuoteResult(insurerName, premium.floatValue()));
        }

        return results;
    }

    // Adjust the premium value if request asks for additional options.
    private float adjustPremium(InsurerInfo insurerInfo, QuoteRequest request, float premium) {
        List<AdditionalOption> checkedOptions = request.getCheckedOptions();
        float adjustedPremium = premium;
        for (AdditionalOption option : checkedOptions) {
            switch (option) {
                case LIVING_BENEFIT:
                    if (insurerInfo.getLivingBenefitOptionAdjustRatio() != 0) {
                        adjustedPremium = adjustedPremium * insurerInfo.getLivingBenefitOptionAdjustRatio();
                    }
                    break;
                case ACCIDENTAL_DEATH_RIDER:
                    if (insurerInfo.getAccidentalRiderAdjustRatio() != 0) {
                        adjustedPremium = adjustedPremium * insurerInfo.getAccidentalRiderAdjustRatio();
                    }
                    break;
                case CHRONICLE_ILLNESS_RIDER:
                    if (insurerInfo.getChronicleIllnessAdjustRatio() != 0) {
                        adjustedPremium = adjustedPremium * insurerInfo.getChronicleIllnessAdjustRatio();
                    }
                    break;
                case CRITICAL_ILLNESS_RIDER:
                    if (insurerInfo.getCriticalIllnessAdjustRatio() != 0) {
                        adjustedPremium = adjustedPremium * insurerInfo.getCriticalIllnessAdjustRatio();
                    }
                    break;
                case TERMINAL_ILLNESS_RIDER:
                    if (insurerInfo.getTerminalIllnessAdjustRatio() != 0) {
                        adjustedPremium = adjustedPremium * insurerInfo.getTerminalIllnessAdjustRatio();
                    }
                    break;
                case CHILDREN_RIDER:
                    if (insurerInfo.getChildrenRiderAdjustRatio() != 0) {
                        adjustedPremium = adjustedPremium * insurerInfo.getChildrenRiderAdjustRatio();
                    }
                    break;
                default:
                    throw new IllegalStateException("Request contain invalid additional option. " + option);
            }
        }

        return adjustedPremium;
    }

    private QuoteResult pickTopAdjustedPremium(List<QuoteResult> quoteResults, QuoteRequest request) {
        if (quoteResults.size() == 0) {
            return null;
        }

        QuoteResult result = quoteResults.get(0);
        float topAjustedPremium = adjustPremium(insurerInfoMap.get(result.getInsurer()), request, result.getPremium());
        for (int i = 1; i < quoteResults.size(); i++) {
            QuoteResult q = quoteResults.get(i);
            float adjustedPremium = adjustPremium(insurerInfoMap.get(q.getInsurer()), request, q.getPremium());
            if (topAjustedPremium > adjustedPremium) {
                topAjustedPremium = adjustedPremium;
                result = q;
            }
        }

        return result;
    }

    // This method picks the top adjusted premium but the insurer must cover at least one of the required options.
    private QuoteResult pickTopAdjustedPremiumWithRequiredOptions(List<QuoteResult> quoteResults, QuoteRequest request, List<AdditionalOption> requiredOptions) {
        if (quoteResults.size() == 0) {
            return null;
        }

        QuoteResult result = null;
        float topAjustedPremium = 0;
        for (int i = 0; i < quoteResults.size(); i++) {
            QuoteResult q = quoteResults.get(i);

            // Check if QuoteResult insurer has covered at least one option in required Options.
            boolean covered = false;
            for (AdditionalOption option : requiredOptions) {
                InsurerInfo info = insurerInfoMap.get(q.getInsurer());
                switch (option) {
                    case LIVING_BENEFIT:
                        if (info.getLivingBenefitOptionAdjustRatio() != 0) {
                            covered = true;
                        }
                        break;
                    case ACCIDENTAL_DEATH_RIDER:
                        if (info.getAccidentalRiderAdjustRatio() != 0) {
                            covered = true;
                        }
                        break;
                    case CHRONICLE_ILLNESS_RIDER:
                        if (info.getChronicleIllnessAdjustRatio() != 0) {
                            covered = true;
                        }
                        break;
                    case CRITICAL_ILLNESS_RIDER:
                        if (info.getCriticalIllnessAdjustRatio() != 0) {
                            covered = true;
                        }
                        break;
                    case TERMINAL_ILLNESS_RIDER:
                        if (info.getTerminalIllnessAdjustRatio() != 0) {
                            covered = true;
                        }
                        break;
                    case CHILDREN_RIDER:
                        if (info.getChildrenRiderAdjustRatio() != 0) {
                            covered = true;
                        }
                        break;
                    default:
                        throw new IllegalStateException("requiredOptions contain invalid additional option. " + option);
                }

                if (covered) {
                    break;
                }
            }

            if (!covered) {
                continue;
            }

            float adjustedPremium = adjustPremium(insurerInfoMap.get(q.getInsurer()), request, q.getPremium());
            if (topAjustedPremium > adjustedPremium || topAjustedPremium == 0) {
                topAjustedPremium = adjustedPremium;
                result = q;
            }
        }

        return result;
    }

    private List<AdditionalOption> getUncoveredOptions(InsurerInfo insurerInfo, List<AdditionalOption> sourceOptions) {
        List<AdditionalOption> uncoveredOptions = new ArrayList<>();
        for (AdditionalOption option : sourceOptions) {
            switch (option) {
                case LIVING_BENEFIT:
                    if (insurerInfo.getLivingBenefitOptionAdjustRatio() == 0) {
                        uncoveredOptions.add(AdditionalOption.LIVING_BENEFIT);
                    }
                    break;
                case ACCIDENTAL_DEATH_RIDER:
                    if (insurerInfo.getAccidentalRiderAdjustRatio() == 0) {
                        uncoveredOptions.add(AdditionalOption.ACCIDENTAL_DEATH_RIDER);
                    }
                    break;
                case CHRONICLE_ILLNESS_RIDER:
                    if (insurerInfo.getChronicleIllnessAdjustRatio() == 0) {
                        uncoveredOptions.add(AdditionalOption.CHRONICLE_ILLNESS_RIDER);
                    }
                    break;
                case CRITICAL_ILLNESS_RIDER:
                    if (insurerInfo.getCriticalIllnessAdjustRatio() == 0) {
                        uncoveredOptions.add(AdditionalOption.CRITICAL_ILLNESS_RIDER);
                    }
                    break;
                case TERMINAL_ILLNESS_RIDER:
                    if (insurerInfo.getTerminalIllnessAdjustRatio() == 0) {
                        uncoveredOptions.add(AdditionalOption.TERMINAL_ILLNESS_RIDER);
                    }
                    break;
                case CHILDREN_RIDER:
                    if (insurerInfo.getChildrenRiderAdjustRatio() == 0) {
                        uncoveredOptions.add(AdditionalOption.CHILDREN_RIDER);
                    }
                    break;
                default:
                    throw new IllegalStateException("sourceOptions contain invalid additional option. " + option);
            }
        }

        return uncoveredOptions;
    }

    // This method returns a hashmap. Key is company name, value is monthly premium.
    // This method will pick top 3 best insurer term products.
    public List<QuoteResult> processRequest(QuoteRequest request) {

        List<QuoteResult> results = new ArrayList<>();
        int age = processAge(request.getBirthday());

        PremiumClass premiumClass = processPremiumClass(request);
        if (premiumClass == PremiumClass.HIGH_RISK) {
            return results;
        }

        List<QuoteResult> quoteResults = getQuoteResults(request.getGender(), age, premiumClass, request.getCoverageTime(), request.getCoverageAmount());
        if (quoteResults.size() <= MAX_NUM_RESULTS) {
            return quoteResults;
        }

        // First, pick one with best adjusted premium.
        QuoteResult topAdjutedPremiumResult = pickTopAdjustedPremium(quoteResults, request);
        results.add(topAdjutedPremiumResult);

        // Remove the one we pick from quoteResults since we don't need to consider it any more.
        if (!quoteResults.remove(topAdjutedPremiumResult)) {
            throw new NoSuchElementException("Cannot find adjusted premium result in quoterults.");
        }

        // Second, pick top one with at least one uncovered option.
        // Check if there are any requested addtional options not being covered by the picked one.
        List<AdditionalOption> uncoveredOptions = getUncoveredOptions(insurerInfoMap.get(topAdjutedPremiumResult.getInsurer()), request.getCheckedOptions());
        if (uncoveredOptions.size() > 0) {
            QuoteResult topAdjutedPremiumResultWithRequiredOptions = pickTopAdjustedPremiumWithRequiredOptions(quoteResults, request, uncoveredOptions);
            if (topAdjutedPremiumResultWithRequiredOptions != null) {
                results.add(topAdjutedPremiumResultWithRequiredOptions);

                // Removed the picked one from quoteResults.
                if (!quoteResults.remove(topAdjutedPremiumResultWithRequiredOptions)) {
                    throw new NoSuchElementException("Cannot find adjusted premium result with required options in quoterults.");
                }
            }
        }

        // Third, fill remaining results with best premium without adjustment.
        Collections.sort(quoteResults, (a, b) -> { return a.getPremium() > b.getPremium() ? 1: -1; });
        int number_to_fill = MAX_NUM_RESULTS - results.size();
        for (int i = 0; i < number_to_fill; i++) {
            // Add the first element and remove the element from quoteResult after adding.
            results.add(quoteResults.remove(0));
        }

        // Now results contains MAX_NUM_RESULTS elements. Now add anything left in quoteResults to tail of results.
        for (int i = 0; i < quoteResults.size(); i++) {
            results.add(quoteResults.get(i));
        }

        return results;
    }

    public List<Long> getCoverageAmountList() {
        // Return a list of valid coverage amounts. The front end needs to let customer select from this list.
        return COVERAGE_AMOUNT_LIST;
    }

    public List<Integer> getCoverageTimeList(int age) {
        // Return a list of coverage time with given age. Currently we make assumption that the valid coverage time list for a given age is same across all insurers.
        if (age >= 20 && age <= 50) {
            return Arrays.asList(10, 15, 20, 30);
        } else if (age >= 51 && age <= 65) {
            return Arrays.asList(10, 15, 20);
        } else if (age >= 66 && age <= 70) {
            return Arrays.asList(10, 15);
        } else if (age >= 71 && age <= 75) {
            return Arrays.asList(10);
        }

        return null;
    }
}
