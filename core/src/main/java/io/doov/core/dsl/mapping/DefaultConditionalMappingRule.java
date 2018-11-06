package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.MappingOperator._else;
import static io.doov.core.dsl.meta.MappingOperator.then;

import io.doov.core.FieldModel;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class DefaultConditionalMappingRule extends AbstractDSLBuilder implements ConditionalMappingRule {

    private static final MappingMetadata thenMetadata = MappingMetadata.mappings(then);
    private static final MappingMetadata elseMetadata = MappingMetadata.mappings(_else);

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
    public Metadata metadata() {
        return thenMetadata;
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
        return this.executeOn(inModel, outModel, new DefaultContext(thenMetadata));
    }

    @Deprecated
    public void accept(MetadataVisitor visitor, int depth) {
        stepWhen.metadata().accept(visitor, depth);

        mappingRules.stream().forEach(r -> {
            visitor.start(thenMetadata, depth);
            visitor.visit(thenMetadata, depth);
            r.metadata().accept(visitor, depth + 1);
            visitor.end(thenMetadata, depth);
        });

        elseMappingRules.stream().forEach(r -> {
            visitor.start(elseMetadata, depth);
            visitor.visit(elseMetadata, depth);
            r.metadata().accept(visitor, depth + 1);
            visitor.end(elseMetadata, depth);
        });
    }
}
