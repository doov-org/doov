package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.ConditionalMappingMetadata.conditional;
import static java.util.stream.Collectors.toList;

import io.doov.core.FieldModel;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class DefaultConditionalMappingRule extends AbstractDSLBuilder implements ConditionalMappingRule {

    private final ConditionalMappingMetadata metadata;

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
        this.metadata = conditional(stepWhen.metadata(),
                        MappingRegistryMetadata
                                        .then(mappingRules.stream().map(MappingRule::metadata).collect(toList())),
                        MappingRegistryMetadata.otherwise(
                                        elseMappingRules.stream().map(MappingRule::metadata).collect(toList())));
    }

    private DefaultConditionalMappingRule(StepWhen stepWhen, MappingRegistry thenRules, MappingRule[] elseRules) {
        this.stepWhen = stepWhen;
        this.validationRule = stepWhen.validate();
        this.mappingRules = thenRules;
        this.elseMappingRules = MappingRegistry.mappings(elseRules);
        this.metadata = conditional(stepWhen.metadata(),
                        MappingRegistryMetadata
                                        .then(mappingRules.stream().map(MappingRule::metadata).collect(toList())),
                        MappingRegistryMetadata.otherwise(
                                        elseMappingRules.stream().map(MappingRule::metadata).collect(toList())));
    }

    @Override
    public Metadata metadata() {
        return metadata;
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
        context.beforeConditionalMapping(this);
        try {
            if (validationRule.executeOn(inModel, context).value()) {
                mappingRules.executeOn(inModel, outModel, context);
            } else if (!elseMappingRules.isEmpty()) {
                elseMappingRules.executeOn(inModel, outModel, context);
            }
        } finally {
            context.afterConditionalMapping(this);
        }
        return context;
    }

    @Override
    public <C extends Context> C executeOn(FieldModel model, C context) {
        return executeOn(model, model, context);
    }

    @Override
    public Context executeOn(FieldModel inModel, FieldModel outModel) {
        return executeOn(inModel, outModel, new DefaultContext(metadata));
    }

    @Override
    public Context executeOn(FieldModel model) {
        return executeOn(model, model);
    }

}
