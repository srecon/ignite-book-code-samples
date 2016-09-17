package com.blu.imdg.example4;

import com.blu.imdg.common.HttpAuditClient;
import org.apache.http.client.HttpClient;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.resources.IgniteInstanceResource;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.XsdValidator;
import com.blu.imdg.example3.ValidateMessage;


import java.util.concurrent.ConcurrentMap;

/**
 * Created by mikl on 12.07.16.
 */
public class ForkJoinJobAdapterExt extends ComputeJobAdapter {

    @IgniteInstanceResource
    private Ignite ignite;

    private ValidateMessage msg;

    public ForkJoinJobAdapterExt(ValidateMessage msg) {
        this.msg = msg;
    }

    @Override
    public Boolean execute() throws IgniteException {
        try {
            boolean validateXsdResult = XsdValidator.validate(msg.getMsg(), msg.getXsd());
            boolean validateByJs = JSEvaluate.evaluateJs(msg.getMsg(), msg.getJs());

            Boolean result = validateXsdResult && validateByJs;

            ConcurrentMap<Object, Object> nodeLocalMap = ignite.cluster().nodeLocalMap();

            HttpClient client = HttpAuditClient.createHttpClient(nodeLocalMap);

            return HttpAuditClient.sendResult(client, result, msg.getId());

        } catch (Exception err) {
            throw new IgniteException(err);
        }
    }

}
