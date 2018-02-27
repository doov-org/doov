package io.doov.core.dsl.mapping;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.ConditionalMappingRule;
import io.doov.core.dsl.lang.MappingRegistry;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.lang.SimpleMappingRule;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultConditionalMappingRule implements ConditionalMappingRule {

    private final ValidationRule validationRule;
    private final List<MappingRule> mappingRules;
    private List<MappingRule> elseMappingRules;

    public DefaultConditionalMappingRule(ValidationRule validationRule, MappingRule... mappingRules) {
        this.validationRule = validationRule;
        this.mappingRules = asList(mappingRules);
        this.elseMappingRules = Collections.emptyList();
    }

    public DefaultConditionalMappingRule(ValidationRule validationRule, MappingRegistry mappingRules) {
        this.validationRule = validationRule;
        this.mappingRules = mappingRules.stream().collect(Collectors.toList());
        this.elseMappingRules = Collections.emptyList();
    }

    @Override
    public ValidationRule validation() {
        return validationRule;
    }

    @Override
    public ConditionalMappingRule otherwise(MappingRule... mappingRule) {
        elseMappingRules = asList(mappingRule);
        return this;
    }

    @Override
    public ConditionalMappingRule otherwise(MappingRegistry mappingRegistry) {
        elseMappingRules = mappingRegistry.stream().collect(Collectors.toList());
        return this;
    }

    @Override
    public boolean validate(FieldModel inModel, FieldModel outModel) {
        return mappingRules.stream().allMatch(m -> m.validate(inModel, outModel))
                        && elseMappingRules.stream().allMatch(m -> m.validate(inModel, outModel));
    }

    @Override
    public void executeOn(FieldModel inModel, FieldModel outModel) {
        if (validationRule.executeOn(inModel).isTrue()) {
            mappingRules.forEach(m -> m.executeOn(inModel, outModel));
        } else {
            elseMappingRules.forEach(m -> m.executeOn(inModel, outModel));
        }
    }

    @Override
    public MappingRule registerOn(MappingRegistry registry) {
        registry.register(this);
        return this;
    }

    @Override
    public String readable() {
        // TODO
        return "if " + validationRule.readable()
                        + " then " + mappingRules.stream().map(Readable::readable).collect(joining(" & "))
                        + " else " + elseMappingRules.stream().map(Readable::readable).collect(joining(" & "));
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        // TODO
    }
}
