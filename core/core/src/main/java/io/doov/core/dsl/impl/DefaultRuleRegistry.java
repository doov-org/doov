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

import java.util.*;
import java.util.stream.Stream;

import io.doov.core.dsl.lang.*;

public class DefaultRuleRegistry implements RuleRegistry {

    public static final RuleRegistry REGISTRY_DEFAULT = new DefaultRuleRegistry();

    private final Map<RuleId, ValidationRule> rules = new HashMap<>();

    @Override
    public void register(ValidationRule rule) {
        register(rule, () -> Integer.toHexString(rule.hashCode()));
    }

    @Override
    public void register(ValidationRule rule, RuleId id) {
        rules.put(id, rule);
    }

    @Override
    public Optional<ValidationRule> get(RuleId id) {
        return Optional.ofNullable(rules.get(id));
    }

    @Override
    public Stream<ValidationRule> stream() {
        return rules.values().stream();
    }

}
