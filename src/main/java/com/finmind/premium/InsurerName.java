package com.finmind.premium;

import java.util.HashMap;
import java.util.Map;

public enum InsurerName {
    COLUMBUS_LIFE("Columbus Life"),
    NATIONWIDE("Nationwide"),
    AIG("AIG"),
    NEW_YORK_LIFE("New York Life");

    private String name;

    private InsurerName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static Map<String, InsurerName> getMap() {
        Map<String, InsurerName> map = new HashMap<>();
        for (InsurerName insurerName: InsurerName.values()) {
            map.put(insurerName.getName(), insurerName);
        }
        return map;
    }

    private static final Map<String, InsurerName> LOOKUP = getMap();

    public static InsurerName getInsurerNameFromStringOrNull(String name) {
        return LOOKUP.get(name);
    }
}
