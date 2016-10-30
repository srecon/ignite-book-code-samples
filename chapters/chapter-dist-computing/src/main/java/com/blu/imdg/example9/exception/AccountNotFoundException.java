package com.blu.imdg.example9.exception;

/**
 * Created by mikl on 29.10.16.
 */
public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
