package io.doov.core.dsl.lang;

import io.doov.core.dsl.DslModel;

/**
 * Interface for the validation rule that encapsulates the validation algorithm and data.
 * <p>
 * This class should be used when keeping references to specific rules.
 */
public interface ValidationRule extends DSLBuilder {

    /**
     * Returns the root when node of this rule.
     *
     * @return the step when
     */
    StepWhen getStepWhen();

    /**
     * Returns a validation rule with the given short circuit.
     *
     * @param shortCircuit the short circuit
     * @return the validation rule
     */
    ValidationRule withShortCircuit(boolean shortCircuit);
    
    
    /**
     * Executes the validation rule on a null model.
     *
     * @return the result
     */
    default Result execute() {
        return executeOn(null);
    }

    /**
     * Executes the validation rule on the given model.
     *
     * @param model the model
     * @return the result
     */
    Result executeOn(DslModel model);

    /**
     * Executes the validation rule on the given model.
     *
     * @param model the model
     * @param context custom context
     * @return the result
     */
    Result executeOn(DslModel model, Context context);

    /**
     * Registers this rule on the given registry.
     *
     * @param registry the registry
     * @return the validation rule
     */
    ValidationRule registerOn(RuleRegistry registry);

}
