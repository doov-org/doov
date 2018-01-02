/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.ast.ResourceProvider;

public enum ResourceBundleProvider implements ResourceProvider {
    BUNDLE;

    private static final String baseName = "io.doov.core.dsl.meta.i18n.DefaultResourceBundle";

    @Override
    public String get(Operator operator) {
        return get(operator, Locale.getDefault());
    }

    @Override
    public String get(Operator operator, Locale locale) {
        final String value;
        try {
            value = ResourceBundle.getBundle(baseName, locale).getString(operator.name());
        } catch (MissingResourceException e) {
            return operator.readable();
        }
        return value;
    }
    
    @Override
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
