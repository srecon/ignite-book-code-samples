package com.blu.imdg.example9;

import com.blu.imdg.example9.exception.AccountNotFoundException;
import com.blu.imdg.example9.exception.LogServiceException;

import java.math.BigDecimal;

/**
 * Created by mikl on 26.10.16.
 */
public interface BankService {
    String NAME = "BANK_SERVICE";

    boolean validateOperation(String account, BigDecimal sum) throws AccountNotFoundException, LogServiceException;
}
