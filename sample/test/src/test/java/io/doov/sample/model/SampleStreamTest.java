package io.doov.sample.model;

import static io.doov.util.LoopingRule.doSomeStuff;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import io.doov.sample.model.SampleModelWrapper;
import io.doov.sample.model.SampleModels;
import io.doov.util.LoopingRule;

public class SampleStreamTest {
    @Rule
    public final TestRule looping = new LoopingRule(10);

    @Test
    public void stream_parallel_apply_wait() {
        new SampleModelWrapper(SampleModels.sample()).parallelStream().map(e -> {
            doSomeStuff();
            return e;
        }).count();
    }


    @Test
    public void stream_sequential_apply_wait() {
        new SampleModelWrapper(SampleModels.sample()).stream().map(e -> {
            doSomeStuff();
            return e;
        }).count();
    }
}
