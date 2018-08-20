package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.MappingMetadata.mappings;
import static io.doov.core.dsl.meta.MappingOperator._else;
import static io.doov.core.dsl.meta.MappingOperator.then;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;

import io.doov.core.FieldModel;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MetadataVisitor;

public class DefaultConditionalMappingRule implements ConditionalMappingRule {

    private final StepWhen stepWhen;
    private final ValidationRule validationRule;
    private final MappingRegistry mappingRules;
    private final MappingRegistry elseMappingRules;

    public DefaultConditionalMappingRule(StepWhen stepWhen, MappingRule... mappingRules) {
        this(stepWhen, mappingRules, new MappingRule[] {});
    }

    private DefaultConditionalMappingRule(StepWhen stepWhen, MappingRule[] thenRules, MappingRule[] elseRules) {
        this.stepWhen = stepWhen;
        this.validationRule = stepWhen.validate();
        this.mappingRules = MappingRegistry.mappings(thenRules);
        this.elseMappingRules = MappingRegistry.mappings(elseRules);
    }

    private DefaultConditionalMappingRule(StepWhen stepWhen, MappingRegistry thenRules, MappingRule[] elseRules) {
        this.stepWhen = stepWhen;
        this.validationRule = stepWhen.validate();
        this.mappingRules = thenRules;
        this.elseMappingRules = MappingRegistry.mappings(elseRules);
    }

    @Override
    public ValidationRule validation() {
        return validationRule;
    }

    @Override
    public ConditionalMappingRule otherwise(MappingRule... elseRules) {
        return new DefaultConditionalMappingRule(stepWhen, mappingRules, elseRules);
    }

    @Override
    public boolean validate(FieldModel inModel, FieldModel outModel) {
        return mappingRules.validate(inModel, outModel) && elseMappingRules.validate(inModel, outModel);
    }

    @Override
    public <C extends Context> C executeOn(FieldModel inModel, FieldModel outModel, C context) {
        if (validationRule.executeOn(inModel, context).isTrue()) {
            mappingRules.executeOn(inModel, outModel, context);
        } else if (!elseMappingRules.isEmpty()) {
            elseMappingRules.executeOn(inModel, outModel, context);
        }
        return context;
    }

    @Override
    public Context executeOn(FieldModel inModel, FieldModel outModel) {
        return this.executeOn(inModel, outModel, new DefaultContext(mappings(then)));
    }

    @Override
    public String readable(Locale locale) {
        return astToString(this, locale);
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        stepWhen.accept(visitor, depth);

        MappingMetadata thenMetadata = mappings(then);
        visitor.start(thenMetadata, depth);
        visitor.visit(thenMetadata, depth);
        mappingRules.stream().forEach(r -> r.accept(visitor, depth + 1));
        visitor.end(thenMetadata, depth);

        if (!elseMappingRules.isEmpty()) {
            MappingMetadata elseMetadata = mappings(_else);
            visitor.start(elseMetadata, depth);
            visitor.visit(elseMetadata, depth);
            elseMappingRules.stream().forEach(r -> r.accept(visitor, depth + 1));
            visitor.end(elseMetadata, depth);
        }
    }
}
