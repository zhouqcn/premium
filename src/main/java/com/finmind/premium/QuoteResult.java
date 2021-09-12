package com.finmind.premium;

public class QuoteResult {
    private InsurerName insurer;

    private float premium;

    public QuoteResult () {}
    public QuoteResult (InsurerName insurer, float premium) {
        this.insurer = insurer;
        this.premium = premium;
    }

    public InsurerName getInsurer() {
        return insurer;
    }

    public void setInsurer(InsurerName insurer) {
        this.insurer = insurer;
    }

    public float getPremium() {
        return premium;
    }

    public void setPremium(float premium) {
        this.premium = premium;
    }
}
