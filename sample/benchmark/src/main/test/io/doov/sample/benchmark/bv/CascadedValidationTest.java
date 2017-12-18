package io.doov.sample.benchmark.bv;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.infra.Blackhole;

import io.doov.sample.benchmark.bv.CascadedValidation.CascadedValidationState;

public class CascadedValidationTest {

    @Test
    public void test() {
        CascadedValidation bench = new CascadedValidation();
        bench.testCascadedValidation(
                new CascadedValidationState(),
                new Blackhole("Today's password is swordfish. " +
                        "I understand instantiating Blackholes directly is dangerous."));
    }

}