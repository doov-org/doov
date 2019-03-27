package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.ConditionalMappingMetadata.conditional;
import static java.util.stream.Collectors.toList;

import io.doov.core.FieldModel;
import io.doov.core.Try;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.*;

public class DefaultConditionalMappingRule implements ConditionalMappingRule {

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
                MappingRegistryMetadata.then(mappingRules.stream().map(MappingRule::metadata).collect(toList())),
                MappingRegistryMetadata.otherwise(elseMappingRules.stream().map(MappingRule::metadata).collect(toList())));
    }

    private DefaultConditionalMappingRule(StepWhen stepWhen, MappingRegistry thenRules, MappingRule[] elseRules) {
        this.stepWhen = stepWhen;
        this.validationRule = stepWhen.validate();
        this.mappingRules = thenRules;
        this.elseMappingRules = MappingRegistry.mappings(elseRules);
        this.metadata = conditional(stepWhen.metadata(),
                MappingRegistryMetadata.then(mappingRules.stream().map(MappingRule::metadata).collect(toList())),
                MappingRegistryMetadata.otherwise(elseMappingRules.stream().map(MappingRule::metadata).collect(toList())));
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
    public <C extends Context> Try<C> executeOn(FieldModel inModel, FieldModel outModel, C context) {
        if (validationRule.executeOn(inModel, context).value()) {
            return mappingRules.executeOn(inModel, outModel, context);
        } else if (!elseMappingRules.isEmpty()) {
            return elseMappingRules.executeOn(inModel, outModel, context);
        } else {
            return Try.success(context);
        }
    }

    @Override
    public Try<Context> executeOn(FieldModel inModel, FieldModel outModel) {
        return this.executeOn(inModel, outModel, new DefaultContext(metadata));
    }

}
