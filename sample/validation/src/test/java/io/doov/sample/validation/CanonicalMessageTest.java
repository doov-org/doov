/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Result;
import io.doov.sample.model.SampleModelWrapper;
import io.doov.sample.model.SampleModels;

public class CanonicalMessageTest {
    private FieldModel model;

    @BeforeAll
    public static void beforeAll() {
        Rules.init();
    }

    @BeforeEach
    public void before() {
        model = new SampleModelWrapper(SampleModels.sample());
    }

    @Test
    public void testMe() {
        Result result = Rules.RULE_ACCOUNT.executeOn(model);
        System.out.println(result.getContext().getValidated());
        System.out.println(result.getContext().getInvalidated());
    }
}
