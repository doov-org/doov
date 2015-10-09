package org.modelmap.sample2.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.modelmap.sample.util.LoopingRule;

public class Sample2StreamParallelTest {
    @Rule
    public final TestRule looping = new LoopingRule(1);

    @Test
    public void stream_parallel_apply_wait() {
        new Sample2ModelWrapper(Sample2Models.sample()).parallelStream().map(e -> {
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
            }
            return e;
        }).count();
    }

    @Test
    public void stream_apply_wait() {
        new Sample2ModelWrapper(Sample2Models.sample()).stream().map(e -> {
            try {
                Thread.sleep(100);
            } catch (Exception ex) {
            }
            return e;
        }).count();
    }
}
