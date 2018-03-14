package io.doov.core.dsl.lang;

import java.util.stream.Stream;

import io.doov.core.FieldModel;

/**
 * Immutable ordered container for {@link MappingRule}s
 */
public interface MappingRegistry {

    /**
     * Create a new registry with all rules of the current registry followed by the rules in given registries
     *
     * @param mappingRegistries mapping registries
     * @return new mapping registry
     */
    MappingRegistry with(MappingRegistry... mappingRegistries);

    /**
     * Create a new registry with all rules of the current registry followed by the given mapping rules
     *
     * @param mappingRules mapping rules
     * @return new mapping registry
     */
    MappingRegistry with(MappingRule... mappingRules);

    /**
     * Stream over mapping rules
     *
     * @return mapping rule stream
     */
    Stream<MappingRule> stream();

    /**
     * Validate and execute rules in this registry with contained order on given models
     *
     * @param inModel  in model
     * @param outModel out model
     */
    default void validateAndExecute(FieldModel inModel, FieldModel outModel) {
        stream().filter(m -> m.validate(inModel, outModel))
                .forEach(m -> m.executeOn(inModel, outModel));
    }
}
