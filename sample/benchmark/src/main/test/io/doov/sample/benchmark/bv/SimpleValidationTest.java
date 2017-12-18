package io.doov.sample.benchmark.bv;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.infra.Blackhole;

import io.doov.sample.benchmark.bv.SimpleValidationWithoutShortCircuit.ValidationState;

public class SimpleValidationTest {

    @Test
    public void test() {
        SimpleValidationWithoutShortCircuit bench = new SimpleValidationWithoutShortCircuit();
        bench.testSimpleBeanValidation(new ValidationState(), new Blackhole("Today's password is swordfish. I " +
                "understand instantiating Blackholes directly is dangerous."));
    }

}