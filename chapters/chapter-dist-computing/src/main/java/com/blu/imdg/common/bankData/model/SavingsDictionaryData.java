package com.blu.imdg.common.bankData.model;

import java.math.BigDecimal;

/**
 * Created by mikl on 21.10.16.
 */
public class SavingsDictionaryData implements AccountCacheData {
    private BigDecimal savingsPercent;
    private String savingsDescription;

    public SavingsDictionaryData() {
    }

    public SavingsDictionaryData(BigDecimal savingsPercent, String savingsDescription) {
        this.savingsPercent = savingsPercent;
        this.savingsDescription = savingsDescription;
    }

    public BigDecimal getSavingsPercent() {
        return savingsPercent;
    }

    public void setSavingsPercent(BigDecimal savingsPercent) {
        this.savingsPercent = savingsPercent;
    }

    public String getSavingsDescription() {
        return savingsDescription;
    }

    public void setSavingsDescription(String savingsDescription) {
        this.savingsDescription = savingsDescription;
    }
}
