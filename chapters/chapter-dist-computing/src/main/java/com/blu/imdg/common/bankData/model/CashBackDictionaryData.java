package com.blu.imdg.common.bankData.model;

import java.math.BigDecimal;

/**
 * Created by mikl on 21.10.16.
 */
public class CashBackDictionaryData implements AccountCacheData {
    private BigDecimal cashBackPercent;
    private String savingsDescription;

    public CashBackDictionaryData() {
    }

    public CashBackDictionaryData(BigDecimal cashBackPercent, String savingsDescription) {
        this.cashBackPercent = cashBackPercent;
        this.savingsDescription = savingsDescription;
    }

    public BigDecimal getCashBackPercent() {
        return cashBackPercent;
    }

    public void setCashBackPercent(BigDecimal cashBackPercent) {
        this.cashBackPercent = cashBackPercent;
    }

    public String getSavingsDescription() {
        return savingsDescription;
    }

    public void setSavingsDescription(String savingsDescription) {
        this.savingsDescription = savingsDescription;
    }
}
