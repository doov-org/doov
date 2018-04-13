/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.mapping;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.meta.MappingMetadata;
import io.doov.core.dsl.meta.MappingOperator;
import io.doov.core.dsl.meta.MetadataVisitor;

/**
 * Immutable, ordered, composable container for {@link MappingRule}s
 */
public class MappingRegistry implements MappingRule {

    private static final MappingMetadata REGISTRY_METADATA = MappingMetadata.mappings(MappingOperator.mappings);

    private final List<MappingRule> mappingRules;

    public static MappingRegistry mappings(MappingRule... mappingRules) {
        return new MappingRegistry(mappingRules);
    }

    private MappingRegistry(MappingRule... mappingRules) {
        this.mappingRules = Arrays.stream(mappingRules).flatMap(MappingRule::stream).collect(Collectors.toList());
    }

    /**
     * Create a new registry with all rules of the current registry followed by the given mapping rules
     *
     * @param rulestoAdd additional mapping rules
     * @return new mapping registry
     */
    public MappingRegistry with(MappingRule... rulestoAdd) {
        return new MappingRegistry(Stream.concat(mappingRules.stream(), Arrays.stream(rulestoAdd))
                .toArray(MappingRule[]::new));
    }

    /**
     * Validate and execute rules in this registry with contained order on given models
     *
     * @param inModel  in model
     * @param outModel out model
     */
    public void validateAndExecute(FieldModel inModel, FieldModel outModel) {
        mappingRules.stream().filter(m -> m.validate(inModel, outModel))
                .forEach(m -> m.executeOn(inModel, outModel));
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
    public void executeOn(FieldModel inModel, FieldModel outModel) {
        for (MappingRule rule : mappingRules) {
            rule.executeOn(inModel, outModel);
        }
    }

    @Override
    public Stream<MappingRule> stream() {
        return mappingRules.stream();
    }

    @Override
    public String readable(Locale locale) {
        return astToString(this, locale);
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        visitor.start(REGISTRY_METADATA, depth);
        visitor.visit(REGISTRY_METADATA, depth);
        mappingRules.forEach(m -> m.accept(visitor, depth + 1));
        visitor.end(REGISTRY_METADATA, depth);
    }
}