package com.blu.imdg.example9;

import com.blu.imdg.example9.exception.LogServiceException;

/**
 * Created by mikl on 30.10.16.
 */
public interface LogService {
    String NAME = "logService";

    void logOperation(String operationCode, Boolean result) throws LogServiceException;
}
