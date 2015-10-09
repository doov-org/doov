package org.modelmap.sample.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.modelmap.sample.util.LoopingRule;

public class SampleStreamParallelTest {
    @Rule
    public final TestRule looping = new LoopingRule(10);

    @Test
    public void stream_parallel_apply_wait() {
        new SampleModelWrapper(SampleModels.sample()).parallelStream().map(e -> {
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
            }
            return e;
        }).count();
    }

    @Test
    public void stream_apply_wait() {
        new SampleModelWrapper(SampleModels.sample()).parallelStream().map(e -> {
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
            }
            return e;
        }).count();
    }
}
