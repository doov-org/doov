package io.doov.core.dsl.lang;

import java.util.stream.Stream;

import io.doov.core.FieldModel;

/**
 *
 */
public interface MappingRegistry {

    void register(MappingRule rule);

    void registerAll(MappingRegistry registry);

    Stream<MappingRule> stream();

    default void validateAndExecute(FieldModel inModel, FieldModel outModel) {
        stream().filter(m -> m.validate(inModel, outModel)).forEach(m -> m.executeOn(inModel, outModel));
    }
}
