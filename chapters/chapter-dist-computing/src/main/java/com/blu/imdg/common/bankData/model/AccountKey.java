package com.blu.imdg.common.bankData.model;

import org.apache.ignite.cache.affinity.AffinityKeyMapped;

import java.io.Serializable;

/**
 * Created by mikl on 21.10.16.
 */
public class AccountKey implements AccountCacheKey,Serializable {
    @AffinityKeyMapped
    private String account;

    public AccountKey(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountKey that = (AccountKey) o;

        return account != null ? account.equals(that.account) : that.account == null;

    }

    @Override
    public int hashCode() {
        return account != null ? account.hashCode() : 0;
    }
}
