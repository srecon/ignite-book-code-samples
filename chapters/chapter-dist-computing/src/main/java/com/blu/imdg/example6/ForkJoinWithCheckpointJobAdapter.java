package com.blu.imdg.example6;

import org.apache.http.client.HttpClient;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.*;
import org.apache.ignite.resources.IgniteInstanceResource;
import org.apache.ignite.resources.JobContextResource;
import org.apache.ignite.resources.TaskSessionResource;
import com.blu.imdg.common.HttpAuditClient;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.XsdValidator;
import com.blu.imdg.example3.ValidateMessage;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by mikl on 12.07.16.
 */
public class ForkJoinWithCheckpointJobAdapter extends ComputeJobAdapter {
    @TaskSessionResource
    private ComputeTaskSession session;

    @JobContextResource
    private ComputeJobContext jobCtx;

    @IgniteInstanceResource
    private Ignite ignite;

    private ValidateMessage msg;

    public ForkJoinWithCheckpointJobAdapter(ValidateMessage msg) {
        this.msg = msg;
    }

    @Override
    public Boolean execute() throws IgniteException {
        try {
            Boolean validateXsdResult = session.loadCheckpoint(msg.getId());

            Boolean checkpointValue = validateXsdResult;
            System.out.println("validateXsdResult=" + validateXsdResult + " msg=" + msg.getId());
            if (validateXsdResult == null) {
                validateXsdResult = XsdValidator.validate(msg.getMsg(), msg.getXsd());
                session.setAttribute(jobCtx.getJobId(), validateXsdResult);
                session.saveCheckpoint(msg.getId(), true);
            }

            if (msg.getId().equals("1") && checkpointValue == null) {
                //emulate error
                System.out.println("throw err!!!");
                throw new ComputeJobFailoverException("err!!");
            }

            System.out.println("real execute msg=" + msg.getId());

            if (!validateXsdResult) {
                System.out.println("force return result false!");
                return sendResultAndReturn(validateXsdResult);
            }

            for (ComputeJobSibling sibling : session.getJobSiblings()) {
                Boolean siblingStep1Result = session.waitForAttribute(sibling.getJobId(), 0);
                if (!siblingStep1Result) {
                    System.out.println("one sibling return false!");
                    return sendResultAndReturn(false);
                }
            }

            boolean validateByJs = JSEvaluate.evaluateJs(msg.getMsg(), msg.getJs());
            return sendResultAndReturn(validateByJs);

        } catch (Exception err) {
            throw new IgniteException(err);
        }
    }

    @NotNull
    private Boolean sendResultAndReturn(Boolean result) throws URISyntaxException, IOException {
        ConcurrentMap<Object, Object> nodeLocalMap = ignite.cluster().nodeLocalMap();
        HttpClient client = HttpAuditClient.createHttpClient(nodeLocalMap);
        return HttpAuditClient.sendResult(client, result, msg.getId());
    }
}
