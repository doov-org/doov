/*
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.modelmap;

import static io.doov.sample.field.SampleFieldIdInfo.age;
import static io.doov.sample.field.SampleFieldIdInfo.drivingLicense;
import static io.doov.sample.field.SampleFieldIdInfo.name;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.*;

public class SimpleValidation {

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
                .validate();

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
        assertThat(result.isTrue()).isEqualTo(driverSetup.expectedResult);
        blackHole.consume(result);
    }

    private class DriverSetup {

        private boolean expectedResult;
        private Driver driver;
        private DslModel model;

        DriverSetup(ValidationState state) {
            expectedResult = true;

            String name = names[state.random.nextInt(10)];
            if (name == null) {
                expectedResult = false;
            }

            int randomAge = state.random.nextInt(100);
            if (randomAge < 18) {
                expectedResult = false;
            }

            int rand = state.random.nextInt(2);
            boolean hasLicense = rand == 1;
            if (!hasLicense) {
                expectedResult = false;
            }

            driver = new Driver(name, randomAge, hasLicense);

            SampleModel model = new SampleModel();
            model.setDriver(driver);

            this.model = new SampleModelWrapper(model);
        }

    }

}
