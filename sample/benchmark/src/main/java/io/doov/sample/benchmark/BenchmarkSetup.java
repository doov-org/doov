/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.benchmark;

import io.doov.benchmark.model.BenchmarkModel;
import io.doov.benchmark.model.Driver;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;

import java.util.Random;
import java.util.function.Function;

public class BenchmarkSetup {

    public interface ValidationState {
        ValidationRule rule();

        Random random();
    }

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

    public boolean expectedResult;
    public int expectedViolationCount;
    public FieldModel model;

    public BenchmarkSetup(Function<BenchmarkModel, FieldModel> wrapperFunction, ValidationState state) {
        expectedResult = true;

        String name = names[state.random().nextInt(10)];
        if (name == null) {
            expectedViolationCount++;
            expectedResult = false;
        }

        int randomAge = state.random().nextInt(100);
        if (randomAge < 18) {
            expectedViolationCount++;
            expectedResult = false;
        }

        int rand = state.random().nextInt(2);
        boolean hasLicense = rand == 1;
        if (!hasLicense) {
            expectedViolationCount++;
            expectedResult = false;
        }

        Driver driver = new Driver(name, randomAge, hasLicense);

        BenchmarkModel model = new BenchmarkModel();
        model.setDriver(driver);

        this.model = wrapperFunction.apply(model);
    }

    public static int getActualViolationCount(Result result) {
        return (int) result.getContext().getEvalFalse().stream()
                .filter(BenchmarkSetup::isLeafBinaryPredicate)
                .count();
    }

    private static boolean isLeafBinaryPredicate(Metadata metadata) {
        return metadata.children()
                .flatMap(Metadata::children)
                .noneMatch(m -> m.type() != MetadataType.LEAF_VALUE);
    }
}
