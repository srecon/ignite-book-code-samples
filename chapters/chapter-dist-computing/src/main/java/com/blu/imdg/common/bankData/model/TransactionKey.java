package com.blu.imdg.common.bankData.model;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mikl on 21.10.16.
 */
public class TransactionKey implements AccountCacheKey, Serializable {

    @QuerySqlField(index = true)
    @AffinityKeyMapped
    private String account;

    private Date transactionDate;

    private String transactionId;

    public TransactionKey() {
    }

    public TransactionKey(String account, Date transactionDate, String transactionId) {
        this.account = account;
        this.transactionDate = transactionDate;
        this.transactionId = transactionId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "TransactionKey{" +
                "account='" + account + '\'' +
                ", transactionDate=" + transactionDate +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
