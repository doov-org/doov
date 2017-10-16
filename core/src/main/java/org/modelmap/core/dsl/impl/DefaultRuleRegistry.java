package org.modelmap.core.dsl.impl;

import java.util.*;
import java.util.stream.Stream;

import org.modelmap.core.dsl.lang.*;

public class DefaultRuleRegistry implements RuleRegistry {

    public static final RuleRegistry REGISTRY = new DefaultRuleRegistry();

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
