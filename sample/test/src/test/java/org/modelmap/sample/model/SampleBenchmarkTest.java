package org.modelmap.sample.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.modelmap.sample.util.LoopingRule;

public class SampleBenchmarkTest {
    private static final int LOOP = 100000;
    @Rule
    public final TestRule looping = new LoopingRule(LOOP);
    private SampleBenchmark bench = new SampleBenchmark();

    @Before
    public void before() {
        bench.init();
    }

    @Test
    public void clone_java_bean() {
        bench.clone_java_bean();
    }

    @Test
    public void clone_by_field_info_stream_sequential() {
        bench.clone_by_field_info_stream_sequential();
    }

    @Test
    public void clone_by_field_info_for() {
        bench.clone_by_field_info_for();
    }

    @Test
    public void clone_by_field_info_for_each() {
        bench.clone_by_field_info_for_each();
    }

    @Test
    public void clone_stream_sequential() {
        bench.clone_stream_sequential();
    }

    @Test
    public void clone_stream_parallel() {
        bench.clone_stream_parallel();
    }
}
