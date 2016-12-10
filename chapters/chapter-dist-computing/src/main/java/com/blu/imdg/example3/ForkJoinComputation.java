package com.blu.imdg.example3;

import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.TestDataGenerator;
import jersey.repackaged.com.google.common.collect.Maps;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.cluster.ClusterTopologyException;
import org.apache.ignite.compute.*;
import org.apache.ignite.resources.LoadBalancerResource;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by mzheludkov on 30.06.16.
 */
//mvn exec:java -Dexec.mainClass=com.blu.imdg.example3.ForkJoinComputation
public class ForkJoinComputation {

    public static void main(String[] args) throws IOException {

        try (Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG)) {

            IgniteCompute compute = ignite.compute();

            ValidateMessage[] validateMessages = TestDataGenerator.getValidateMessages();

            Boolean result = compute.execute(new ComputeTask<ValidateMessage[], Boolean>() {
                @LoadBalancerResource
                private ComputeLoadBalancer balancer;

                @Nullable
                @Override
                public Map<? extends ComputeJob, ClusterNode> map(List<ClusterNode> list, @Nullable ValidateMessage[] validateMessages) throws IgniteException {
                    Map<ComputeJob, ClusterNode> result = Maps.newHashMap();
                    for (ValidateMessage msg : validateMessages) {
                        ComputeJobAdapter job = new ForkJoinJobAdapter(msg);
                        ClusterNode balancedNode = balancer.getBalancedNode(job, null);
                        result.put(job, balancedNode);
                    }
                    return result;
                }

                @Override
                public ComputeJobResultPolicy result(ComputeJobResult computeJobResult, List<ComputeJobResult> list) throws IgniteException {
                    IgniteException e = computeJobResult.getException();
                    if (e != null) {
                        if (!(e instanceof ComputeExecutionRejectedException) && !(e instanceof ClusterTopologyException) && !e.hasCause(new Class[]{ComputeJobFailoverException.class})) {
                            throw new IgniteException("Remote job threw user exception (override or implement ComputeTask.result(..) method if you would like to have automatic failover for this exception).", e);
                        } else {
                            return ComputeJobResultPolicy.FAILOVER;
                        }
                    } else {
                        return ComputeJobResultPolicy.WAIT;
                    }
                }

                @Nullable
                @Override
                public Boolean reduce(List<ComputeJobResult> list) throws IgniteException {
                    return list.stream().reduce(true, (acc, value) -> acc && (Boolean) value.getData(), (a, b) -> a && b);
                }
            }, validateMessages);


            System.out.println("result=" + result);

        }

    }

}
