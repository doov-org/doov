/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.core.dsl.meta.i18n;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import io.doov.core.dsl.meta.Operator;

public enum ResourceBundleProvider implements ResourceProvider {

    BUNDLE;

    private final List<String> providers = new CopyOnWriteArrayList<>();

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
        if (key == null)
            return null;
        try {
            return ResourceBundle.getBundle(name, locale).getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

}
