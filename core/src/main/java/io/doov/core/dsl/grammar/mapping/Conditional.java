/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import io.doov.core.dsl.grammar.Value;

public class Conditional extends Value<Void> {
    public final Value<Boolean> condition;
    public final Value<Void> caseTrue;
    public final Value<Void> caseFalse;

    public Conditional(Value<Boolean> condition, Value<Void> caseTrue, Value<Void> caseFalse) {
        this.condition = condition;
        this.caseTrue = caseTrue;
        this.caseFalse = caseFalse;
    }

    @Override
    public String toString() {
        return "Conditional(" +
                " when : " + condition.toString() +
                ", then : " + caseTrue.toString() +
                ", otherwise : " + caseFalse.toString() + ")";
    }
}
