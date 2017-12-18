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

import static io.doov.benchmark.model.BenchmarkFieldIdInfo.age;
import static io.doov.benchmark.model.BenchmarkFieldIdInfo.drivingLicense;
import static io.doov.benchmark.model.BenchmarkFieldIdInfo.name;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import io.doov.benchmark.model.*;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.FieldMetadata;

/*
 * http://in.relation.to/2017/10/31/bean-validation-benchmark-revisited/
 */
public class SimpleValidationWithoutShortCircuit {

    private static final String[] names = {
            null,
            "Jacob",
            "Isabella",
            "Ethan",
            "Sophia",
            "Michael",
            "Emma",
            "Jayden",
            "Olivia",
            "William"
    };

    @State(Scope.Benchmark)
    public static class ValidationState {

        volatile ValidationRule rule = DOOV
                .when(name().isNotNull()
                        .and(age().greaterOrEquals(18))
                        .and(drivingLicense().isTrue()))
                .validate()
                .withShortCircuit(false);

        volatile Random random = new Random();

    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Threads(50)
    @Warmup(iterations = 10)
    @Measurement(iterations = 20)
    public void testSimpleBeanValidation(ValidationState state, Blackhole blackHole) {
        DriverSetup driverSetup = new DriverSetup(state);
        Result result = state.rule.executeOn(driverSetup.model);
        assertThat(getActualViolationCount(result)).isEqualTo(driverSetup.expectedViolationCount);
        blackHole.consume(result);
    }

    private class DriverSetup {

        private int expectedViolationCount;
        private Driver driver;
        private DslModel model;

        DriverSetup(ValidationState state) {
            expectedViolationCount = 0;

            String name = names[state.random.nextInt(10)];
            if (name == null) {
                expectedViolationCount++;
            }

            int randomAge = state.random.nextInt(100);
            if (randomAge < 18) {
                expectedViolationCount++;
            }

            int rand = state.random.nextInt(2);
            boolean hasLicense = rand == 1;
            if (!hasLicense) {
                expectedViolationCount++;
            }

            driver = new Driver(name, randomAge, hasLicense);

            BenchmarkModel model = new BenchmarkModel();
            model.setDriver(driver);

            this.model = new BenchmarkModelWrapper(model);
        }

    }

    private static int getActualViolationCount(Result result) {
        return (int) result.getFailedNodes().stream()
                .filter(metadata -> metadata instanceof FieldMetadata)
                .count();
    }

}
