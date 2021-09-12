package com.finmind.premium;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestUtil {
    @Test
    public void testCurrencyConvert() throws Exception{
        final String a = "$199";
        final String b = "$199.00";
        final String c = "$100,000,000";
        final String d = "199.3 dollars";
        final String e = "$  199.10";
        final String f = "？ 199.20";

        assertEquals(Util.currencyToLong(a), Long.valueOf(199));
        assertEquals(Util.currencyToLong(b), Long.valueOf(199));
        assertEquals(Util.currencyToLong(c), Long.valueOf(100000000L));
        assertEquals(Util.currencyToLong(d), Long.valueOf(199));
        assertEquals(Util.currencyToLong(e), Long.valueOf(199));
        assertEquals(Util.currencyToLong(f), Long.valueOf(199));
    }
}
