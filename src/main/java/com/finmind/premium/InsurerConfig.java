package com.finmind.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class InsurerConfig {
    @JsonProperty("LivingBenefitOptionAdjustRatio")
    private float livingBenefitOptionAdjustRatio;

    @JsonProperty("AccidentalRiderAdjustRatio")
    private float accidentalRiderAdjustRatio;

    @JsonProperty("ChronicleIllnessAdjustRatio")
    private float chronicleIllnessAdjustRatio;

    @JsonProperty("CriticalIllnessAdjustRatio")
    private float criticalIllnessAdjustRatio;

    @JsonProperty("TerminalIllnessAdjustRatio")
    private float terminalIllnessAdjustRatio;

    @JsonProperty("ChildrenRiderAdjustRatio")
    private float childrenRiderAdjustRatio;

    @JsonProperty("PremiumConfigs")
    private HashSet<PremiumConfig> premiumConfigs;

    public InsurerConfig() {}

    public InsurerConfig(float livingBenefitOptionAdjustRatio,
                         float accidentalRiderAdjustRatio,
                         float chronicleIllnessAdjustRatio,
                         float criticalIllnessAdjustRatio,
                         float terminalIllnessAdjustRatio,
                         float childrenRiderAdjustRatio,
                         HashSet<PremiumConfig> premiumConfigs) {
        this.livingBenefitOptionAdjustRatio = livingBenefitOptionAdjustRatio;
        this.accidentalRiderAdjustRatio = accidentalRiderAdjustRatio;
        this.chronicleIllnessAdjustRatio = chronicleIllnessAdjustRatio;
        this.criticalIllnessAdjustRatio = criticalIllnessAdjustRatio;
        this.terminalIllnessAdjustRatio = terminalIllnessAdjustRatio;
        this.childrenRiderAdjustRatio = childrenRiderAdjustRatio;
        this.premiumConfigs = premiumConfigs;
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

    public HashSet<PremiumConfig> getPremiumConfigs() {
        return premiumConfigs;
    }
}
