/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import io.doov.core.dsl.meta.Operator;

public enum OperatorResourceBundle {
    BUNDLE;
    
    private static final String baseName = "io.doov.core.dsl.meta.i18n.OperatorResourceBundle";
    
    public String get(Operator operator) {
        return ResourceBundle.getBundle(baseName).getString(operator.name());
    }
    
    public String get(Operator operator, Locale locale) {
        return ResourceBundle.getBundle(baseName, locale).getString(operator.name());
    }
}
