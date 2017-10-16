package org.modelmap;

import org.modelmap.sample.model.SampleModels;
import org.modelmap.sample.validation.Registry;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.logic.BlackHole;

@Fork(value = 5)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
public class BenchmarkRule {

    @GenerateMicroBenchmark
    public void valid_country(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_COUNTRY.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @GenerateMicroBenchmark
    public void valid_email(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_EMAIL.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

}
