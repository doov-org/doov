package io.doov.sample.benchmark.bv;

import static io.doov.benchmark.model.BenchmarkFieldIdInfo.*;
import static io.doov.core.dsl.DOOV.matchAll;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import io.doov.benchmark.model.*;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;

/*
 * http://in.relation.to/2017/10/31/bean-validation-benchmark-revisited/
 */
public class CascadedValidation {

    @State(Scope.Benchmark)
    public static class CascadedValidationState {

        volatile ValidationRule rule = DOOV
                .when(matchAll(
                        friendName1().isNotNull(),
                        friendName2().isNotNull(),
                        friendName3().isNotNull(),
                        friendName4().isNotNull(),
                        friendName5().isNotNull(),
                        friendName6().isNotNull()))
                .validate();

    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Threads(50)
    @Warmup(iterations = 10)
    @Measurement(iterations = 20)
    public void testCascadedValidation(CascadedValidationState state, Blackhole blackhole) {
        DriverSetup driverSetup = new DriverSetup();
        Result result = state.rule.executeOn(driverSetup.model);
        assertThat(result.isTrue()).isTrue();
        blackhole.consume(result);
    }

    public class DriverSetup {

        private Driver driver;
        private DslModel model;

        DriverSetup() {
            driver = new Driver("driver", 18, true);

            Friend kermit = new Friend("kermit");
            Friend piggy = new Friend("miss piggy");
            Friend gonzo = new Friend("gonzo");

            driver.addFriend(piggy);
            driver.addFriend(gonzo);
            driver.addFriend(kermit);
            driver.addFriend(gonzo);
            driver.addFriend(kermit);
            driver.addFriend(piggy);

            BenchmarkModel model = new BenchmarkModel();
            model.setDriver(driver);

            this.model = new BenchmarkModelWrapper(model);
        }

    }

}
