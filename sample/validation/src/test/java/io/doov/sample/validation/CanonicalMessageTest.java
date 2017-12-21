/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
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
        Rules.RULE_ACCOUNT.executeOn(model).getInvalidated();
    }
}
