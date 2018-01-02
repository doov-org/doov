/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import java.util.Locale;

import io.doov.core.dsl.meta.Operator;

public class ReadableResourceProvider implements ResourceProvider {

    @Override
    public String get(Operator operator) {
        return operator.readable();
    }

    @Override
    public String get(Operator operator, Locale locale) {
        return operator.readable();
    }

    @Override
    public String get(String key, Locale locale) {
        return key;
    }

}
