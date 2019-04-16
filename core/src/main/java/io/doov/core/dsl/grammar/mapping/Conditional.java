/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.grammar.mapping;

import static io.doov.core.dsl.utils.JsonGrammar.JBind;
import static io.doov.core.dsl.utils.JsonGrammar.JNode;
import static io.doov.core.dsl.utils.JsonGrammar.JObject;
import static io.doov.core.dsl.utils.JsonGrammar.JString;

import io.doov.core.dsl.grammar.ASTNode;

public class Conditional extends ASTNode<Void> {
    public final ASTNode<Boolean> condition;
    public final ASTNode<Void> caseTrue;
    public final ASTNode<Void> caseFalse;

    public Conditional(ASTNode<Boolean> condition, ASTNode<Void> caseTrue, ASTNode<Void> caseFalse) {
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
    public JNode json() {
        return new JObject(
                new JBind("meta", new JString("CONDITIONAL")),
                new JBind("when", condition.json()),
                new JBind("then", caseTrue.json()),
                new JBind("else", caseFalse.json())
        );
    }
}
