/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.doov.core.FieldModel;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingRegistryMetadata;
import io.doov.core.dsl.meta.Metadata;

/**
 * Immutable, ordered, composable container for {@link MappingRule}s
 */
public class MappingRegistry extends AbstractDSLBuilder implements MappingRule {

    private final List<MappingRule> mappingRules;
    private final MappingRegistryMetadata metadata;

    public static MappingRegistry mappings(MappingRule... mappingRules) {
        return new MappingRegistry(mappingRules);
    }

    private MappingRegistry(MappingRule... mappingRules) {
        this.mappingRules = Arrays.asList(mappingRules);
        // TODO
        this.metadata = MappingRegistryMetadata
                        .mappings(stream().map(MappingRule::metadata).collect(Collectors.toList()));
    }

    @Override
    public Metadata metadata() {
        return metadata;
    }

    /**
     * Create a new registry with all rules of the current registry followed by the given mapping rules
     *
     * @param rulestoAdd additional mapping rules
     * @return new mapping registry
     */
    public MappingRegistry with(MappingRule... rulestoAdd) {
        return new MappingRegistry(
                        Stream.concat(mappingRules.stream(), Arrays.stream(rulestoAdd)).toArray(MappingRule[]::new));
    }

    /**
     * Validate and execute rules in this registry with contained order on given models
     *
     * @param inModel  in model
     * @param outModel out model
     * @return context
     */
    public Context validateAndExecute(FieldModel inModel, FieldModel outModel) {
        DefaultContext context = new DefaultContext(metadata());
        mappingRules.stream().filter(m -> m.validate(inModel, outModel))
                        .forEach(m -> m.executeOn(inModel, outModel, context));
        return context;
    }

    /**
     * Validate and execute rules in this registry with contained order on given models
     *
     * @param inModel  in model
     * @param outModel out model
     * @param context  context
     * @param <C>      context type
     * @return context
     */
    public <C extends Context> C validateAndExecute(FieldModel inModel, FieldModel outModel, C context) {
        context.beforeMappingRegistry(this);
        try {
            mappingRules.stream().filter(m -> m.validate(inModel, outModel))
                            .forEach(m -> m.executeOn(inModel, outModel, context));
        } finally {
            context.afterMappingRegistry(this);
        }
        return context;
    }

    /**
     * @return true if registry is empty
     */
    public boolean isEmpty() {
        return mappingRules.isEmpty();
    }

    @Override
    public boolean validate(FieldModel inModel, FieldModel outModel) {
        return mappingRules.stream().allMatch(m -> m.validate(inModel, outModel));
    }

    @Override
    public <C extends Context> C executeOn(FieldModel inModel, FieldModel outModel, C context) {
        context.beforeMappingRegistry(this);
        try {
            mappingRules.forEach(rule -> rule.executeOn(inModel, outModel, context));
        } finally {
            context.afterMappingRegistry(this);
        }
        return context;
    }

    @Override
    public <C extends Context> C executeOn(FieldModel model, C context) {
        return executeOn(model, model, context);
    }

    @Override
    public Context executeOn(FieldModel inModel, FieldModel outModel) {
        return executeOn(inModel, outModel, new DefaultContext(metadata()));
    }

    @Override
    public Context executeOn(FieldModel model) {
        return executeOn(model, model, new DefaultContext(metadata()));
    }

    @Override
    public Stream<MappingRule> stream() {
        return mappingRules.stream();
    }

}
