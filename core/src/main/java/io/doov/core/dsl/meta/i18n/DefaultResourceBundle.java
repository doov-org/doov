/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.i18n;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.*;

import io.doov.core.dsl.meta.Operator;

public enum DefaultResourceBundle {
    BUNDLE;

    private static final String baseName = "io.doov.core.dsl.meta.i18n.OperatorResourceBundle";

    public String get(Operator operator) {
        return get(operator, Locale.getDefault());
    }

    public String get(Operator operator, Locale locale) {
        final String value;
        try {
            value = ResourceBundle.getBundle(baseName, locale).getString(operator.name());
        } catch (MissingResourceException e) {
            return operator.readable();
        }
        if (isEmpty(value))
            return operator.readable();
        return value;
    }
}
