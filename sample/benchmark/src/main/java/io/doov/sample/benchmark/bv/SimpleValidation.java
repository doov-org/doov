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

import io.doov.benchmark.model.BenchmarkModelWrapper;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.benchmark.BenchmarkSetup;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.doov.benchmark.model.dsl.DslBenchmarkModel.*;
import static io.doov.sample.benchmark.BenchmarkSetup.getActualViolationCount;
import static org.assertj.core.api.Assertions.assertThat;

/*
 * http://in.relation.to/2017/10/31/bean-validation-benchmark-revisited/
 */
public class SimpleValidation {

    @State(Scope.Benchmark)
    public static class ValidationState implements BenchmarkSetup.ValidationState {

        volatile ValidationRule rule = DOOV
                .when(name.isNotNull()
                        .and(age.greaterOrEquals(18))
                        .and(drivingLicense.isTrue()))
                .validate();

        volatile Random random = new Random();

        @Override
        public ValidationRule rule() {
            return rule;
        }

        @Override
        public Random random() {
            return random;
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Threads(50)
    @Warmup(iterations = 5)
    @Measurement(iterations = 5)
    public void testSimpleBeanValidation(ValidationState state, Blackhole blackHole) {
        BenchmarkSetup benchmarkSetup = new BenchmarkSetup(BenchmarkModelWrapper::new, state);
        Result result = state.rule.executeOn(benchmarkSetup.model);
        assertThat(result.value()).isEqualTo(benchmarkSetup.expectedResult);
        if (result.value()) {
            assertThat(getActualViolationCount(result)).isEqualTo(0);
        } else {
            assertThat(getActualViolationCount(result)).isEqualTo(1);
        }
        blackHole.consume(result);
    }



}
