/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import static io.doov.core.dsl.utils.JsonGrammar.*;

import io.doov.core.dsl.grammar.Value;
import io.doov.core.dsl.utils.JsonGrammar;

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

    @Override
    public JNode jsonNode() {
        return new JObject(
                new JBind("meta", new JString("CONDITIONAL")),
                new JBind("when", condition.jsonNode()),
                new JBind("then", caseTrue.jsonNode()),
                new JBind("else", caseFalse.jsonNode())
        );
    }
}
