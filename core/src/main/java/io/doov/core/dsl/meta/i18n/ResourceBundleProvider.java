/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.i18n;

import java.util.*;

import io.doov.core.dsl.meta.Operator;

public enum ResourceBundleProvider implements ResourceProvider {

    BUNDLE;

    private final List<String> providers = new ArrayList<>();

    ResourceBundleProvider() {
        register("io.doov.core.dsl.meta.i18n.DefaultResourceBundle");
    }

    public void register(String bundleBaseName) {
        if (!providers.contains(bundleBaseName)) {
            providers.add(bundleBaseName);
        }
    }

    @Override
    public String get(Operator operator, Locale locale) {
        return providers.stream()
                .map(name -> getString(operator.name(), locale, name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(operator.readable());
    }

    @Override
    public String get(String key, Locale locale) {
        return providers.stream()
                .map(name -> getString(key, locale, name))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(key);
    }

    private String getString(String key, Locale locale, String name) {
        try {
            return ResourceBundle.getBundle(name, locale).getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

}
