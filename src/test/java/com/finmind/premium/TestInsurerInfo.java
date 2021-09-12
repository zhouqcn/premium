package com.finmind.premium;

import org.junit.Test;

import java.util.HashSet;

public class TestInsurerInfo {
    @Test
    public void testInsurerInfo() throws Exception {
        PremiumConfig config = new PremiumConfig("Female", "10", "Standard", "test_cl_f_10.csv");
        HashSet<PremiumConfig> premiumConfigs = new HashSet<>();
        premiumConfigs.add(config);

        InsurerConfig insurerConfig = new InsurerConfig(0.9f, 0.9f, 0.9f, 0.9f, 0.9f, 0.9f, premiumConfigs);
        InsurerInfo insurerInfo = new InsurerInfo(insurerConfig);
    }
}
