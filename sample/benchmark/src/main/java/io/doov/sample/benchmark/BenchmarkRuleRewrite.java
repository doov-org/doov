package io.doov.sample.benchmark;

import static io.doov.sample.field.dsl.DslSampleModel.accountCompany;
import static io.doov.sample.field.dsl.DslSampleModel.when;
import static io.doov.sample.model.Company.*;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import io.doov.sample.field.dsl.DslSampleModel.SampleModelRule;
import io.doov.sample.model.SampleModel;
import io.doov.sample.model.SampleModels;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(value = 1)
@Threads(4)
@Warmup(iterations = 5)
@Measurement(iterations = 20)
public class BenchmarkRuleRewrite {
    private static SampleModel sample = SampleModels.sample();
    static {
        sample.getAccount().setCompany(DAILYMOTION);
    }
    private static final SampleModelRule benchRule1 = when(accountCompany.noneMatch(DAILYMOTION, BLABLACAR)).validate();
    private static final SampleModelRule benchRule2 = when(
            accountCompany.notEq(DAILYMOTION).and(accountCompany.notEq(BLABLACAR))).validate();
    private static final SampleModelRule benchRule3 = when(
            accountCompany.eq(DAILYMOTION).or(accountCompany.eq(BLABLACAR)).not()).validate();
    private static final SampleModelRule benchRule4 = when(
            accountCompany.anyMatch(LES_FURETS, CANAL_PLUS, MEETIC, OODRIVE)).validate();
    private static final SampleModelRule benchRule5 = when(
            accountCompany.eq(LES_FURETS).or(accountCompany.eq(CANAL_PLUS)
                    .or(accountCompany.eq(MEETIC).or(accountCompany.eq(OODRIVE))))).validate();

    @Benchmark
    public void test_account(Blackhole blackhole) {
        boolean valid = benchRule1.withShortCircuit(false).executeOn(sample).value();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @Benchmark
    public void test_account_short_circuit(Blackhole blackhole) {
        boolean valid = benchRule1.executeOn(sample).value();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @Benchmark
    public void test_account_2(Blackhole blackhole) {
        boolean valid = benchRule2.withShortCircuit(false).executeOn(sample).value();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @Benchmark
    public void test_account_2_short_circuit(Blackhole blackhole) {
        boolean valid = benchRule2.executeOn(sample).value();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @Benchmark
    public void test_account_3(Blackhole blackhole) {
        boolean valid = benchRule3.withShortCircuit(false).executeOn(sample).value();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @Benchmark
    public void test_account_3_short_circuit(Blackhole blackhole) {
        boolean valid = benchRule3.executeOn(sample).value();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @Benchmark
    public void test_account_4(Blackhole blackhole) {
        boolean valid = benchRule4.withShortCircuit(false).executeOn(sample).value();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @Benchmark
    public void test_account_4_short_circuit(Blackhole blackhole) {
        boolean valid = benchRule4.executeOn(sample).value();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @Benchmark
    public void test_account_5(Blackhole blackhole) {
        boolean valid = benchRule5.withShortCircuit(false).executeOn(sample).value();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }

    @Benchmark
    public void test_account_5_short_circuit(Blackhole blackhole) {
        boolean valid = benchRule5.executeOn(sample).value();
        if (blackhole != null) {
            blackhole.consume(valid);
        }
    }
}
