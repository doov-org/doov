package org.modelmap;

import org.modelmap.sample.model.SampleModels;
import org.modelmap.sample.validation.RulesOld;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.logic.BlackHole;

@Fork(value = 5)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
public class BenchmarkOldRule {

    @GenerateMicroBenchmark
    public void valid_country(BlackHole blackhole) {
        boolean valid = RulesOld.validateCountry(SampleModels.sample().getAccount());
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @GenerateMicroBenchmark
    public void valid_email(BlackHole blackhole) {
        boolean valid = RulesOld.validateEmail(SampleModels.sample().getAccount());
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

}
