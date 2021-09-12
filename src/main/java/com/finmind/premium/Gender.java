package com.finmind.premium;

import java.util.HashMap;
import java.util.Map;

public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private String name;

    private Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    private static Map<String, Gender> getMap() {
        Map<String, Gender> map = new HashMap<>();
        for (Gender gender: Gender.values()) {
            map.put(gender.getName(), gender);
        }
        return map;
    }

    private static final Map<String, Gender> LOOKUP = getMap();

    public static Gender getGenderFromStringOrNull(String name) {
        return LOOKUP.get(name);
    }
}
