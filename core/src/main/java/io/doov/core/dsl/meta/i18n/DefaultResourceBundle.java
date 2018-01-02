/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import io.doov.core.dsl.meta.Operator;

public enum DefaultResourceBundle {
    BUNDLE;

    private static final String baseName = "io.doov.core.dsl.meta.i18n.DefaultResourceBundle";

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
        return value;
    }
    
    public String get(String key, Locale locale) {
        final String value;
        try {
            value = ResourceBundle.getBundle(baseName, locale).getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
        return value;
    }
}
