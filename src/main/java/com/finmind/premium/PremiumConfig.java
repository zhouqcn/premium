package com.finmind.premium;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PremiumConfig {
    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Term")
    private String term;

    @JsonProperty("Class")
    private String premiumClass;

    @JsonProperty("Source")
    private String source;

    public String getGender() {
        return gender;
    }

    public PremiumConfig() {}

    public PremiumConfig(String gender,
                         String term,
                         String premiumClass,
                         String source) {
        this.gender = gender;
        this.term = term;
        this.premiumClass = premiumClass;
        this.source = source;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getPremiumClass() {
        return premiumClass;
    }

    public void setPremiumClass(String premiumClass) {
        this.premiumClass = premiumClass;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
