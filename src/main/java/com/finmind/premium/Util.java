package com.finmind.premium;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Util {
    private static BigDecimal currencyToNumber(String amount) throws ParseException {
        final NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        ((DecimalFormat)format).setParseBigDecimal(true);
        return (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]",""));
    }

    public static Long currencyToLong(String amount) throws ParseException {
        return currencyToNumber(amount).longValue();
    }

}
