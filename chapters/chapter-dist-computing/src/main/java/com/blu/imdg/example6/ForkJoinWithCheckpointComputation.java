package com.blu.imdg.example6;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSessionFullSupport;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.apache.ignite.configuration.CacheConfiguration;
import com.blu.imdg.common.TestDataGenerator;
import com.blu.imdg.example3.ValidateMessage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.blu.imdg.common.CommonConstants.CLIENT_CONFIG;

/**
 * Created by mikl on 14.07.16.
 */
@ComputeTaskSessionFullSupport
public class ForkJoinWithCheckpointComputation extends ComputeTaskSplitAdapter<ValidateMessage[], Boolean> {


    @Override
    protected Collection<? extends ComputeJob> split(int i, ValidateMessage[] messages) throws IgniteException {
        return Arrays.stream(messages).map(ForkJoinWithCheckpointJobAdapter::new).collect(Collectors.toList());
    }


    @Nullable
    @Override
    public Boolean reduce(List<ComputeJobResult> list) throws IgniteException {
        return list.stream().reduce(true, (acc, value) -> acc && (Boolean) value.getData(), (a, b) -> a && b);
    }

    public static void main(String[] args) throws IOException {

        try (Ignite ignite = Ignition.start(CLIENT_CONFIG)) {
            IgniteCompute compute = ignite.compute();
            CacheConfiguration cacheConfiguration = new CacheConfiguration("checkpoints");
            ignite.getOrCreateCache(cacheConfiguration);

            ValidateMessage[] validateMessages = TestDataGenerator.getValidateMessages();

            Boolean result = compute.execute(new ForkJoinWithCheckpointComputation(), validateMessages);
            System.out.println("final result=" + result);
        }
    }
}
