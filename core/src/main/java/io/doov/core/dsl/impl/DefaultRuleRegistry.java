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
package io.doov.core.dsl.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.doov.core.dsl.lang.RuleRegistry;
import io.doov.core.dsl.lang.ValidationRule;

public class DefaultRuleRegistry implements RuleRegistry {

    public static final RuleRegistry REGISTRY_DEFAULT = new DefaultRuleRegistry();

    private final List<ValidationRule> rules = new ArrayList<>();

    @Override
    public void register(ValidationRule rule) {
        rules.add(rule);
    }

    @Override
    public Stream<ValidationRule> stream() {
        return rules.stream();
    }

}
