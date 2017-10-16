/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.lang;

import java.util.Optional;
import java.util.stream.Stream;

public interface RuleRegistry {

    void register(ValidationRule rule);

    void register(ValidationRule rule, RuleId id);

    Optional<ValidationRule> get(RuleId id);

    Stream<ValidationRule> stream();

}
