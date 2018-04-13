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
        if (!providers.contains(bundleBaseName))
            providers.add(bundleBaseName);
    }

    @Override
    public String get(Operator operator, Locale locale) {
        return providers.stream().map(b -> {
            try {
                return ResourceBundle.getBundle(b, locale).getString(operator.name());
            } catch (MissingResourceException e) {
                return null;
            }
        }).filter(Objects::nonNull).findFirst().orElse(operator.readable());
    }

    @Override
    public String get(String key, Locale locale) {
        return providers.stream().map(b -> {
            try {
                final ResourceBundle bundle = ResourceBundle.getBundle(b, locale);
                return bundle.getString(key);
            } catch (MissingResourceException e) {
                return null;
            }
        }).filter(Objects::nonNull).findFirst().orElse(key);
    }
}
