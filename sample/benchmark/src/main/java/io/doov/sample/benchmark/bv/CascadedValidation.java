package io.doov.sample.benchmark.bv;

import static io.doov.sample.field.SampleFieldIdInfo.name;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.sample.model.*;

/**
 * @author Hardy Ferentschik
 */
public class CascadedValidation {

    private static final String[] names = {
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
    public static class CascadedValidationState {

        volatile ValidationRule rule = DOOV.when(name().isNotNull()).validate();

        volatile Random random = new Random();

    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Fork(value = 1)
    @Threads(50)
    @Warmup(iterations = 10)
    @Measurement(iterations = 20)
    public void testCascadedValidation(CascadedValidationState state, Blackhole bh) {
        //        DriverSetup driverSetup = new DriverSetup(state);
        //        Res
        //
        //        // TODO graphs needs to be generated and deeper
        //        Person kermit = new Person("kermit");
        //        Person piggy = new Person("miss piggy");
        //        Person gonzo = new Person("gonzo");
        //
        //        kermit.addFriend(piggy).addFriend(gonzo);
        //        piggy.addFriend(kermit).addFriend(gonzo);
        //        gonzo.addFriend(kermit).addFriend(piggy);
        //
        //        state.rule.executeOn(state)
        //
        //        Set<ConstraintViolation<Person>> violations = state.validator.validate(kermit);
        //        assertThat(violations).hasSize(0);
        //
        //        bh.consume(violations);
    }

    public class DriverSetup {

        private Driver driver;
        private DslModel model;

        DriverSetup(CascadedValidationState state) {
            String name = names[state.random.nextInt(10)];

            Driver driver = new Driver();
            driver.setName(name);

            SampleModel model = new SampleModel();
            model.setDriver(driver);

            this.model = new SampleModelWrapper(model);
        }

    }

}
