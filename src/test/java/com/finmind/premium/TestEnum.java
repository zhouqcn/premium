package com.finmind.premium;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestEnum {
    @Test
    public void testGender() {
        Gender gender = Gender.MALE;
        assertEquals(Gender.getGenderFromStringOrNull("Male"), Gender.MALE);
        assertEquals(gender.getName(), "Male");
    }

    @Test
    public void testInsurerName() {
        InsurerName insurer = InsurerName.COLUMBUS_LIFE;
        assertEquals(InsurerName.getInsurerNameFromStringOrNull("Columbus Life"), InsurerName.COLUMBUS_LIFE);
        assertEquals(insurer.getName(), "Columbus Life");
    }

    @Test
    public void testPremiumClass() {
        PremiumClass premiumClass = PremiumClass.STANDARD;
        assertEquals(PremiumClass.getPremiumClassFromStringOrNull("Standard"), PremiumClass.STANDARD);
        assertEquals(premiumClass.getName(), "Standard");
    }
}
