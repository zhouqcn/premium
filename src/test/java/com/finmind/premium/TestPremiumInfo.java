package com.finmind.premium;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestPremiumInfo {
    @Test
    public void testPremiumInfo() throws Exception {
        PremiumConfig config = new PremiumConfig("Female", "10", "Standard", "test_cl_f_10.csv");
        PremiumInfo info = new PremiumInfo(config);
        assertEquals(info.getPremium(Integer.valueOf(21), Long.valueOf(200000L)), Float.valueOf(19.6f));
        assertNull(info.getPremium(Integer.valueOf(19), Long.valueOf(200000)));
        assertNull(info.getPremium(Integer.valueOf(21), Long.valueOf(1000)));
    }
}
