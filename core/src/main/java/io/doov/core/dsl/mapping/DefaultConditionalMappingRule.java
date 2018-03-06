package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.MappingMetadata.mappings;
import static io.doov.core.dsl.meta.MappingOperator._else;
import static io.doov.core.dsl.meta.MappingOperator.then;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;
import static java.util.Arrays.asList;

import java.util.*;
import java.util.stream.Collectors;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class DefaultConditionalMappingRule implements ConditionalMappingRule {

    private final StepWhen stepWhen;
    private final ValidationRule validationRule;
    private final List<MappingRule> mappingRules;
    private final List<MappingRule> elseMappingRules;

    public DefaultConditionalMappingRule(StepWhen stepWhen, MappingRule... mappingRules) {
        this(stepWhen, asList(mappingRules), Collections.emptyList());
    }

    public DefaultConditionalMappingRule(StepWhen stepWhen, MappingRegistry mappingRules) {
        this(stepWhen, mappingRules.stream().collect(Collectors.toList()), Collections.emptyList());
    }

    public DefaultConditionalMappingRule(StepWhen stepWhen, List<MappingRule> thenRules, List<MappingRule> elseRules) {
        this.stepWhen = stepWhen;
        this.validationRule = stepWhen.validate();
        this.mappingRules = thenRules;
        this.elseMappingRules = elseRules;
    }

    @Override
    public ValidationRule validation() {
        return validationRule;
    }

    @Override
    public ConditionalMappingRule otherwise(MappingRule... elseRules) {
        return new DefaultConditionalMappingRule(stepWhen, mappingRules, asList(elseRules));
    }

    @Override
    public ConditionalMappingRule otherwise(MappingRegistry mappingRegistry) {
        return new DefaultConditionalMappingRule(stepWhen, mappingRules,
                        mappingRegistry.stream().collect(Collectors.toList()));
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
        return astToString(this, Locale.getDefault());
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        stepWhen.accept(visitor, depth);

        MappingMetadata thenMetadata = mappings(then);
        visitor.start(thenMetadata, depth);
        visitor.visit(thenMetadata, depth);
        mappingRules.forEach(r -> r.accept(visitor, depth + 1));
        visitor.end(thenMetadata, depth);

        if (elseMappingRules.size() > 0) {
            MappingMetadata elseMetadata = mappings(_else);
            visitor.start(elseMetadata, depth);
            visitor.visit(elseMetadata, depth);
            elseMappingRules.forEach(r -> r.accept(visitor, depth + 1));
            visitor.end(elseMetadata, depth);
        }
    }
}
