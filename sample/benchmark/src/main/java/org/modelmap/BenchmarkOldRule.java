package org.modelmap;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.logic.BlackHole;

import io.doov.sample.model.SampleModels;
import io.doov.sample.validation.RulesOld;

@Fork(value = 5)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
public class BenchmarkOldRule {

    public void valid_email(BlackHole blackhole) {
        boolean valid = RulesOld.validateEmail(SampleModels.sample().getAccount());
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    public void valid_country(BlackHole blackhole) {
        boolean valid = RulesOld.validateCountry(SampleModels.sample().getAccount());
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

}
