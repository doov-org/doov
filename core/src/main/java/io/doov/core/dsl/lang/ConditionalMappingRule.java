package io.doov.core.dsl.lang;

/**
 * Conditional mapping rule
 * The conditional mapping rule will execute when this validation rule is valid.
 */
public interface ConditionalMappingRule extends MappingRule {

    /**
     * Validation rule
     *
     * @return validation rule
     */
    ValidationRule validation();

    /**
     * Adds rules to execute when the validation rule is invalid
     *
     * @param mappingRule mapping rules
     * @return itself
     */
    ConditionalMappingRule otherwise(MappingRule... mappingRule);

    /**
     * Adds rules to execute when the validation rule is invalid
     *
     * @param mappingRegistry mapping registry
     * @return itself
     */
    ConditionalMappingRule otherwise(MappingRegistry mappingRegistry);
}
