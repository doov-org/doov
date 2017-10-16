package org.modelmap;

import org.modelmap.sample.model.SampleModels;
import org.modelmap.sample.validation.Registry;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.logic.BlackHole;

public class BenchmarkRule {

    public void valid_email(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_EMAIL.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    public void valid_country(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_COUNTRY.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @GenerateMicroBenchmark
    public void valid_country_20(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_COUNTRY_20.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @GenerateMicroBenchmark
    public void valid_country_40(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_COUNTRY_40.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @GenerateMicroBenchmark
    public void valid_country_60(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_COUNTRY_60.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @GenerateMicroBenchmark
    public void valid_country_80(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_COUNTRY_80.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @GenerateMicroBenchmark
    public void valid_country_100(BlackHole blackhole) {
        boolean valid = Registry.ACCOUNT_VALID_COUNTRY_100.executeOn(SampleModels.wrapper()).isValid();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

}
