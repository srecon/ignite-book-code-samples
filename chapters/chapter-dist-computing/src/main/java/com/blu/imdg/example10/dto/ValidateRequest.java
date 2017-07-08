package com.blu.imdg.example10.dto;

import java.math.BigDecimal;

/**
 * Created by mikl on 07.01.17.
 */
public class ValidateRequest {
    String account;
    BigDecimal sum;

    public ValidateRequest() {
    }

    public ValidateRequest(String account, BigDecimal sum) {
        this.account = account;
        this.sum = sum;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "ValidateRequest{" +
                "account='" + account + '\'' +
                ", sum=" + sum +
                '}';
    }
}
