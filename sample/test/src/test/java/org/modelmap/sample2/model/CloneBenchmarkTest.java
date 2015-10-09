package org.modelmap.sample2.model;

import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runners.model.Statement;
import org.modelmap.sample2.model.CloneBenchmark;

public class CloneBenchmarkTest {
    private static final int LOOP = 1000;

    @Rule
    public TestRule chrono = (base, description) -> new Statement() {
        @Override
        public void evaluate() throws Throwable {
            final long startTime = System.nanoTime();
            for (int i = 0; i < LOOP; i++)
                base.evaluate();
            final long elapsedTime = System.nanoTime() - startTime;
            System.out.println(description.getMethodName() + " " + elapsedTime / 1000 + " micros");
        }
    };

    private CloneBenchmark bench = new CloneBenchmark();

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
    public void clone_stream_sequential() {
        bench.clone_stream_sequential();
    }

    @Test
    public void clone_stream_parallel() {
        bench.clone_stream_parallel();
    }
}
