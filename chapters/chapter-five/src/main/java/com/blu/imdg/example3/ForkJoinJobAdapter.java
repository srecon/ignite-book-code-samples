package com.blu.imdg.example3;

import org.apache.ignite.IgniteException;
import org.apache.ignite.compute.ComputeJobAdapter;
import com.blu.imdg.common.JSEvaluate;
import com.blu.imdg.common.XsdValidator;

/**
 * Created by mikl on 23.08.16.
 */
public class ForkJoinJobAdapter extends ComputeJobAdapter {

    private ValidateMessage msg;

    public ForkJoinJobAdapter(ValidateMessage msg) {
        this.msg = msg;
    }

    @Override
    public Boolean execute() throws IgniteException {
        boolean validateXsdResult = XsdValidator.validate(msg.getMsg(), msg.getXsd());
        boolean validateByJs = JSEvaluate.evaluateJs(msg.getMsg(), msg.getJs());

        System.out.println("msg=" + msg.getId());
        System.out.println("validateXsdResult=" + validateXsdResult);
        System.out.println("validateByJs=" + validateByJs);

        return validateXsdResult && validateByJs;
    }
}