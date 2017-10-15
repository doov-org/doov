/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.core.dsl.lang;

import java.util.stream.Stream;

public interface RuleRegistry {

    void register(ValidationRule rule);

    Stream<ValidationRule> stream();

}
