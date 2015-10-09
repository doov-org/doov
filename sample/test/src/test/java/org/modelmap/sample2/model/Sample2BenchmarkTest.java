package org.modelmap.sample2.model;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.modelmap.sample.util.LoopingRule;

public class Sample2BenchmarkTest {
    private static final int LOOP = 100;
    @Rule
    public final TestRule looping = new LoopingRule(LOOP);
    private final Sample2Benchmark bench = new Sample2Benchmark();

    @Before
    public void before() {
        bench.init();
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
    public void clone_stream_sequential_and_collect() {
        bench.clone_stream_sequential_and_collect();
    }

    @Test
    public void clone_stream_parallel_and_collect() {
        bench.clone_stream_parallel_and_collect();
    }

    @Test
    public void clone_stream_sequential_for_each() {
        bench.clone_stream_sequential_for_each();
    }

    @Test
    public void clone_stream_parallel_for_each() {
        bench.clone_stream_parallel_for_each();
    }
}
