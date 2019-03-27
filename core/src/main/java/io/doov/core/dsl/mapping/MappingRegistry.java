/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.doov.core.FieldModel;
import io.doov.core.Try;
import io.doov.core.dsl.impl.DefaultContext;
import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.meta.MappingRegistryMetadata;
import io.doov.core.dsl.meta.Metadata;

/**
 * Immutable, ordered, composable container for {@link MappingRule}s
 */
public class MappingRegistry implements MappingRule {

    private final List<MappingRule> mappingRules;
    private final MappingRegistryMetadata metadata;

    public static MappingRegistry mappings(MappingRule... mappingRules) {
        return new MappingRegistry(mappingRules);
    }

    private MappingRegistry(MappingRule... mappingRules) {
        this.mappingRules = Arrays.asList(mappingRules);
        // TODO
        this.metadata = MappingRegistryMetadata.mappings(stream().map(MappingRule::metadata).collect(Collectors.toList()));
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
     * @return true if registry is empty
     */
    public boolean isEmpty() {
        return mappingRules.isEmpty();
    }

    @Override
    public <C extends Context> Try<C> executeOn(FieldModel inModel, FieldModel outModel, C context) {
        return mappingRules.stream()
                .map(rule -> rule.executeOn(inModel, outModel, context))
                .findFirst().get();
    }

    @Override
    public Try<Context> executeOn(FieldModel inModel, FieldModel outModel) {
        return this.executeOn(inModel, outModel, new DefaultContext(metadata()));
    }

    @Override
    public Stream<MappingRule> stream() {
        return mappingRules.stream();
    }

}
