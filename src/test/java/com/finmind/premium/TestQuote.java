package com.finmind.premium;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;

import java.lang.reflect.Method;
import static org.junit.Assert.*;

import java.util.*;

public class TestQuote {
    private static Logger LOGGER = LoggerFactory.getLogger(TestQuote.class);
    QuoteRequest testRequest = null;
    Quote testQuote = new Quote();

    @Before
    public void setup() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR) - 20, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));  // Set age of 20.
        Date birthday = cal.getTime();
        List<AdditionalOption> options = Arrays.asList(AdditionalOption.LIVING_BENEFIT, AdditionalOption.ACCIDENTAL_DEATH_RIDER, AdditionalOption.CHRONICLE_ILLNESS_RIDER);
        testRequest = new QuoteRequest(
                Gender.MALE,
                birthday,
                true,
                true,
                true,
                LivingStyleAnswer.NEVER,
                LivingStyleAnswer.RARELY,
                LivingStyleAnswer.NEVER,
                false,
                false,
                options,
                200000,
                10
        );

        Config config = Config.readConfig("test_products.json");
        testQuote.loadDataFromConfig(config);
    }

    @Test
    public void testProcessAge() throws Exception {
        Quote q = new Quote();
        Method m = q.getClass().getDeclaredMethod("processAge", Date.class);
        m.setAccessible(true);

        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR) - 10, cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        Date date = cal.getTime();
        int a = (int) m.invoke(q, date);

        assertEquals(a, 10);
    }

    @Test
    public void testProcessPremiumClass() throws Exception {
        Quote q = new Quote();
        Method m = q.getClass().getDeclaredMethod("processPremiumClass", QuoteRequest.class);
        m.setAccessible(true);

        PremiumClass premiumClass = (PremiumClass)m.invoke(q, testRequest);
        assertEquals(premiumClass.getName(), "Standard");
    }

    @Test
    public void testGetQuoteResults() throws Exception {
        Config config = Config.readConfig("test_products.json");
        Quote quote = new Quote();
        quote.loadDataFromConfig(config);

        Method m = quote.getClass().getDeclaredMethod("getQuoteResults", Gender.class, int.class, PremiumClass.class, int.class, Long.class);
        m.setAccessible(true);

        List<QuoteResult> result1 = (List<QuoteResult>)m.invoke(quote, Gender.MALE, 20, PremiumClass.STANDARD, 10, 200000L);
        assertEquals(4, result1.size());
    }

    @Test
    public void testAdjustPremium() throws Exception {
        PremiumConfig config = new PremiumConfig("Female", "10", "Standard", "test_cl_f_10.csv");
        HashSet<PremiumConfig> premiumConfigs = new HashSet<>();
        premiumConfigs.add(config);

        InsurerConfig insurerConfig = new InsurerConfig(0.5f, 0.5f, 0.8f, 0.9f, 0.9f, 0.9f, premiumConfigs);
        InsurerInfo insurerInfo = new InsurerInfo(insurerConfig);

        Quote quote = new Quote();
        Method m = quote.getClass().getDeclaredMethod("adjustPremium", InsurerInfo.class, QuoteRequest.class, float.class);
        m.setAccessible(true);
        float adjustedPremium = (float)m.invoke(quote, insurerInfo, testRequest, 1.0f);
        assertTrue(0.2f == adjustedPremium);
    }

    @Test
    public void testPickTopAdjustedPremium() throws Exception {
        List<QuoteResult> quoteResults = new ArrayList<>();
        quoteResults.add(new QuoteResult(InsurerName.COLUMBUS_LIFE, 10));
        quoteResults.add(new QuoteResult(InsurerName.AIG, 10));
        quoteResults.add(new QuoteResult(InsurerName.NATIONWIDE, 10));

        Method m = testQuote.getClass().getDeclaredMethod("pickTopAdjustedPremium", List.class, QuoteRequest.class);
        m.setAccessible(true);
        QuoteResult result = (QuoteResult) m.invoke(testQuote, quoteResults, testRequest);

        assertEquals(result.getInsurer(), InsurerName.NATIONWIDE);
        assertTrue(result.getPremium() == 10f);
    }

    @Test
    public void testPickTopAdjustedPremiumWithRequiredOptions() throws Exception {
        List<QuoteResult> quoteResults = new ArrayList<>();
        quoteResults.add(new QuoteResult(InsurerName.COLUMBUS_LIFE, 10));
        quoteResults.add(new QuoteResult(InsurerName.AIG, 10));
        quoteResults.add(new QuoteResult(InsurerName.NATIONWIDE, 10));

        List<AdditionalOption> requiredOptions = new ArrayList<>();
        requiredOptions.add(AdditionalOption.TERMINAL_ILLNESS_RIDER);
        requiredOptions.add(AdditionalOption.CHILDREN_RIDER);

        Method m = testQuote.getClass().getDeclaredMethod("pickTopAdjustedPremiumWithRequiredOptions", List.class, QuoteRequest.class, List.class);
        m.setAccessible(true);
        QuoteResult result = (QuoteResult) m.invoke(testQuote, quoteResults, testRequest, requiredOptions);

        assertEquals(result.getInsurer(), InsurerName.AIG);
        assertTrue(result.getPremium() == 10f);
    }

    @Test
    public void testGetUncoveredOptions() throws Exception {
        List<AdditionalOption> sourceOptions = new ArrayList<>();
        sourceOptions.add(AdditionalOption.ACCIDENTAL_DEATH_RIDER);
        sourceOptions.add(AdditionalOption.TERMINAL_ILLNESS_RIDER);
        sourceOptions.add(AdditionalOption.CHRONICLE_ILLNESS_RIDER);
        sourceOptions.add(AdditionalOption.CHILDREN_RIDER);

        PremiumConfig config = new PremiumConfig("Female", "10", "Standard", "test_cl_f_10.csv");
        HashSet<PremiumConfig> premiumConfigs = new HashSet<>();
        premiumConfigs.add(config);

        InsurerConfig insurerConfig = new InsurerConfig(0.9f, 0.9f, 0.9f, 0.9f, 0.9f, 0f, premiumConfigs);
        InsurerInfo insurerInfo = new InsurerInfo(insurerConfig);

        Quote q = new Quote();
        Method m = q.getClass().getDeclaredMethod("getUncoveredOptions", InsurerInfo.class, List.class);
        m.setAccessible(true);
        List<AdditionalOption> uncoveredOptions = (List<AdditionalOption>)m.invoke(q, insurerInfo, sourceOptions);

        assertEquals(uncoveredOptions.size(), 1);
        assertEquals(uncoveredOptions.get(0), AdditionalOption.CHILDREN_RIDER);
    }

    @Test
    public void testProcessRequest() throws Exception {
        List<QuoteResult> results = testQuote.processRequest(testRequest);
        Collections.sort(results, (a, b) -> { return a.getPremium() > b.getPremium() ? 1 : -1; });
        assertEquals(results.size(), 3);
        assertEquals(results.get(0).getInsurer(), InsurerName.AIG);
        assertEquals(results.get(1).getInsurer(), InsurerName.NEW_YORK_LIFE);
        assertEquals(results.get(2).getInsurer(), InsurerName.COLUMBUS_LIFE);
    }

}
