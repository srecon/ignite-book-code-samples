package com.blu.imdg.example9;

import com.blu.imdg.common.HttpAuditClient;
import com.blu.imdg.example9.exception.LogServiceException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import java.io.IOException;

/**
 * Created by mikl on 30.10.16.
 */
public class LogServiceImpl implements LogService, Service {
    private CloseableHttpClient auditClient;

    @Override
    public void logOperation(String operationCode, Boolean result) throws LogServiceException {
        try {
            HttpAuditClient.sendResult(auditClient, result, operationCode);
        }catch (Exception err) {
            throw new LogServiceException(err);
        }
    }

    @Override
    public void cancel(ServiceContext serviceContext) {
        try {
            auditClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServiceContext serviceContext) throws Exception {
        auditClient = (CloseableHttpClient) HttpAuditClient.createHttpClient();
    }

    @Override
    public void execute(ServiceContext serviceContext) throws Exception {

    }
}
