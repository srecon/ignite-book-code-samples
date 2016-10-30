package com.blu.imdg.common.bankData.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.math.BigDecimal;

/**
 * Created by mikl on 21.10.16.
 */
public class TransactionData implements AccountCacheData {


    private String fromAccount;
    private String toAccount;
    private BigDecimal sum;
    private String transactionType;

    public TransactionData() {
    }

    public TransactionData(String fromAccount, String toAccount, BigDecimal sum, String transactionType) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.sum = sum;
        this.transactionType = transactionType;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "TransactionData{" +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", sum=" + sum +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}
