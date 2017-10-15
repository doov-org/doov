package org.modelmap.sample.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.modelmap.core.dsl.lang.RuleRegistry;
import org.modelmap.core.dsl.lang.ValidationRule;

public enum Registry implements RuleRegistry {

    ACCOUNT;

    private List<ValidationRule> rules = new ArrayList<>();

    @Override
    public void register(ValidationRule rule) {
        rules.add(rule);
    }

    @Override
    public Stream<ValidationRule> stream() {
        return rules.stream();
    }

}
