package com.finmind.premium;

import java.text.ParseException;
import java.util.HashMap;
import java.io.*;

public class PremiumInfo {
    private Gender gender;

    private int term;

    private PremiumClass premiumClass;

    private static final String COMMA_DELIMITER = ",";

    public static String makeKey(Gender gender, int term, PremiumClass premiumClass) {
        return gender.toString() + "-" + term + "-" + premiumClass.toString();
    }

    private HashMap<Pair<Integer, Long>, Float> premiumTable = new HashMap<>();

    public Gender getGender() {
        return gender;
    }

    public int getTerm() {
        return term;
    }

    public PremiumClass getPremiumClass() {
        return premiumClass;
    }

    public PremiumInfo(PremiumConfig config) throws Exception {
        this.gender = Gender.getGenderFromStringOrNull(config.getGender());
        if (this.gender == null) {
            throw new IllegalArgumentException("gender string is invalid in config.");
        }

        this.term = Integer.parseInt(config.getTerm());
        this.premiumClass = PremiumClass.getPremiumClassFromStringOrNull(config.getPremiumClass());
        if (this.premiumClass == null) {
            throw new IllegalArgumentException("premium class string is invalid in config.");
        }

        loadPremiumSource(config.getSource());
    }

    // This method loads premium data from csv files to table.
    // It converts all types like currency to numbers.
    // The first row is coverage amount in dollars (Long). The first column is insured age (Integer).
    // Save premium number to hash map while the key is <age, coverage amount> pair.
    private void loadPremiumSource(String fileName) throws IOException, ParseException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        String absolutePath = file.getAbsolutePath();

        try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))) {
            String line;
            // Read the first line to get coverage amount.
            line = br.readLine();
            String[] coverages = line.split(COMMA_DELIMITER);
            while ((line = br.readLine()) != null) {
                String[] premiums = line.split(COMMA_DELIMITER);
                for (int i = 1; i < premiums.length; i++) {
                    Pair<Integer, Long> pair = new Pair<>(Integer.valueOf(premiums[0]), Util.currencyToLong(coverages[i]));
                    premiumTable.put(pair, Float.valueOf(premiums[i]));
                }
            }
        }
    }

    public Float getPremium(Integer age, Long coverageAmount) {
        return premiumTable.get(new Pair<Integer, Long>(age, coverageAmount));
    }
}
