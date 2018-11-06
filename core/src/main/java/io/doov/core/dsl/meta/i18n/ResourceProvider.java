/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.i18n;

import java.util.Locale;

import io.doov.core.dsl.meta.Operator;

public interface ResourceProvider {
    String get(Operator operator, Locale locale);

    String get(String key, Locale locale);
}
