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
package io.doov.core.dsl.lang;

import java.util.stream.Stream;

/**
 * Interface for the rules registry.
 * <p>
 * You can add a rule directly in the DSL with the method {@link ValidationRule#registerOn(RuleRegistry)}.
 */
public interface RuleRegistry {

    /**
     * Adds the given rule to this registry.
     *
     * @param rule the rule to register
     */
    void register(ValidationRule rule);

    /**
     * Stream the rules of this registry.
     *
     * @return the rule stream
     */
    Stream<ValidationRule> stream();

}
