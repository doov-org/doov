package io.doov.core.dsl.lang;

public interface ConditionalMappingRule extends MappingRule {

    ValidationRule validation();

    ConditionalMappingRule otherwise(MappingRule... mappingRule);

    ConditionalMappingRule otherwise(MappingRegistry mappingRegistry);
}
