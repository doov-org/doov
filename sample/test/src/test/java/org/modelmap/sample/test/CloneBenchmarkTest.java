package org.modelmap.sample.test;

import static java.util.Arrays.stream;
import static org.modelmap.sample.model.FavoriteWebsite.webSite;

import java.util.ArrayList;

import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.modelmap.sample.model.CloneBenchmark;

public class CloneBenchmarkTest {
    private static final int LOOP = 100000;

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

    private CloneBenchmark clone = new CloneBenchmark();

    @Before
    public void before() {
        clone.init();
    }

    @Test
    public void clone_java_bean() {
        clone.clone_java_bean();
    }

    @Test
    public void clone_by_fieldid() {
        clone.clone_by_fieldid();
    }

    @Test
    public void clone_field_model() {
        clone.clone_field_model();
    }

    @Test
    public void clone_stream_sequential() {
        clone.clone_stream_sequential();
    }

    @Test
    public void clone_stream_parallel() {
        clone.clone_stream_parallel();
    }
}
