/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.sample.benchmark.bv;

import static io.doov.benchmark.model.dsl.DslBenchmarkModel.*;
import static io.doov.core.dsl.DOOV.matchAll;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import io.doov.benchmark.model.*;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
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
                        friendName1.isNotNull(),
                        friendName2.isNotNull(),
                        friendName3.isNotNull(),
                        friendName4.isNotNull(),
                        friendName5.isNotNull(),
                        friendName6.isNotNull()))
                .validate();

    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Threads(50)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    public void testCascadedValidation(CascadedValidationState state, Blackhole blackhole) {
        DriverSetup driverSetup = new DriverSetup();
        Result result = state.rule.executeOn(driverSetup.model);
        assertThat(result.value()).isTrue();
        blackhole.consume(result);
    }

    public class DriverSetup {

        private Driver driver;
        private FieldModel model;

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
