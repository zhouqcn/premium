package com.finmind.premium;

import java.util.HashMap;
import java.util.Map;

public enum PremiumClass {
    PREFERRED("Preferred"),
    STANDARD("Standard"),
    HIGH_RISK("High_Risk");

    private String name;

    private PremiumClass(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static Map<String, PremiumClass> getMap() {
        Map<String, PremiumClass> map = new HashMap<>();
        for (PremiumClass premiumClass: PremiumClass.values()) {
            map.put(premiumClass.getName(), premiumClass);
        }
        return map;
    }

    private static final Map<String, PremiumClass> LOOKUP = getMap();

    public static PremiumClass getPremiumClassFromStringOrNull(String name) {
        return LOOKUP.get(name);
    }
}
