package com.finmind.premium;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class InsurerInfo {
    private float livingBenefitOptionAdjustRatio;

    private float accidentalRiderAdjustRatio;

    private float chronicleIllnessAdjustRatio;

    private float criticalIllnessAdjustRatio;

    private float terminalIllnessAdjustRatio;

    private float childrenRiderAdjustRatio;

    private HashMap<String, PremiumInfo> PremiumTables = new HashMap<>();

    public InsurerInfo(InsurerConfig config) throws Exception {
        this.livingBenefitOptionAdjustRatio = config.getLivingBenefitOptionAdjustRatio();
        this.accidentalRiderAdjustRatio = config.getAccidentalRiderAdjustRatio();
        this.chronicleIllnessAdjustRatio = config.getChronicleIllnessAdjustRatio();
        this.criticalIllnessAdjustRatio = config.getCriticalIllnessAdjustRatio();
        this.terminalIllnessAdjustRatio = config.getTerminalIllnessAdjustRatio();
        this.childrenRiderAdjustRatio = config.getChildrenRiderAdjustRatio();

        HashSet<PremiumConfig> configs = config.getPremiumConfigs();
        Iterator<PremiumConfig> it = configs.iterator();
        while (it.hasNext()) {
            PremiumConfig c = it.next();
            PremiumInfo i = new PremiumInfo(c);
            PremiumTables.put(PremiumInfo.makeKey(i.getGender(), i.getTerm(), i.getPremiumClass()), i);
        }
    }

    public PremiumInfo getPremiumInfo(Gender gender, int term, PremiumClass premiumClass) {
        return PremiumTables.get(PremiumInfo.makeKey(gender, term, premiumClass));
    }

    public float getLivingBenefitOptionAdjustRatio() {
        return livingBenefitOptionAdjustRatio;
    }

    public float getAccidentalRiderAdjustRatio() {
        return accidentalRiderAdjustRatio;
    }

    public float getChronicleIllnessAdjustRatio() {
        return chronicleIllnessAdjustRatio;
    }

    public float getCriticalIllnessAdjustRatio() {
        return criticalIllnessAdjustRatio;
    }

    public float getTerminalIllnessAdjustRatio() {
        return terminalIllnessAdjustRatio;
    }

    public float getChildrenRiderAdjustRatio() {
        return childrenRiderAdjustRatio;
    }
}
