package com.finmind.premium;

import org.w3c.dom.Document;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class QuoteRequest {
    private Gender gender;

    private Date birthday;

    private boolean haveCriticalIllness;

    private boolean haveChronicIllness;

    private boolean haveGeneticillnessInFamily;

    private LivingStyleAnswer smoke;

    private LivingStyleAnswer marijuana;

    private LivingStyleAnswer drug;

    private boolean militaryService;

    private boolean highRiskActivity;

    private List<AdditionalOption> checkedOptions;

    private long coverageAmount;

    private int coverageTime;

    private void validateRequest() {
        if (birthday.after(Date.from(Instant.now()))) {
            throw new IllegalArgumentException("birthday is invalid");
        }
    }

    public QuoteRequest(Gender gender,
                        Date birthday,
                        boolean haveCriticalIllness,
                        boolean haveChronicIllness,
                        boolean haveGeneticillnessInFamily,
                        LivingStyleAnswer smoke,
                        LivingStyleAnswer marijuana,
                        LivingStyleAnswer drug,
                        boolean militaryService,
                        boolean highRiskActivity,
                        List<AdditionalOption> checkedOptions,
                        long coverageAmount,
                        int coverageTime) {
        this.gender = gender;
        this.birthday = birthday;
        this.haveCriticalIllness = haveCriticalIllness;
        this.haveChronicIllness = haveChronicIllness;
        this.haveGeneticillnessInFamily = haveGeneticillnessInFamily;
        this.smoke = smoke;
        this.marijuana = marijuana;
        this.drug = drug;
        this.militaryService = militaryService;
        this.highRiskActivity = highRiskActivity;
        this.checkedOptions = checkedOptions;
        this.coverageAmount = coverageAmount;
        this.coverageTime = coverageTime;
    }

    public QuoteRequest(Document document) {
        // Supplies XML document to construct object.

        // validateRequest();
    }

    public Gender getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public boolean isHaveCriticalIllness() {
        return haveCriticalIllness;
    }

    public boolean isHaveChronicIllness() {
        return haveChronicIllness;
    }

    public boolean isHaveGeneticillnessInFamily() {
        return haveGeneticillnessInFamily;
    }

    public LivingStyleAnswer getSmoke() {
        return smoke;
    }

    public LivingStyleAnswer getMarijuana() {
        return marijuana;
    }

    public LivingStyleAnswer getDrug() {
        return drug;
    }

    public boolean isMilitaryService() {
        return militaryService;
    }

    public boolean isHighRiskActivity() {
        return highRiskActivity;
    }

    public List<AdditionalOption> getCheckedOptions() {
        return checkedOptions;
    }

    public long getCoverageAmount() {
        return coverageAmount;
    }

    public int getCoverageTime() {
        return coverageTime;
    }
}
