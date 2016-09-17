package com.blu.imdg.example7;

import com.blu.imdg.common.HttpAuditClient;
import com.blu.imdg.common.XsdValidator;
import com.blu.imdg.example3.ValidateMessage;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;


import java.io.IOException;

/**
 * Created by mikl on 07.08.16.
 */
public class XsdValidatingServiceImpl implements XsdValidatingService, Service {

    private CloseableHttpClient auditClient;

    @Override
    public boolean isOk(ValidateMessage msg) {
        Boolean validateXsdResult = XsdValidator.validate(msg.getMsg(), msg.getXsd());
        sendResult(msg, validateXsdResult);
        return validateXsdResult;
    }

    private void sendResult(ValidateMessage msg, Boolean validateXsdResult) {
        try {
            HttpAuditClient.sendResult(auditClient, validateXsdResult, msg.getId());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    //service methods

    @Override
    public void cancel(ServiceContext serviceContext) {
        System.out.println("cancel service");
        try {
            auditClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(ServiceContext serviceContext) throws Exception {
        System.out.println("init service");
        auditClient = (CloseableHttpClient) HttpAuditClient.createHttpClient();
    }

    @Override
    public void execute(ServiceContext serviceContext) throws Exception {
    }
}
