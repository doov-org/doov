/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.assertions.ts;

import java.util.*;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;

import io.doov.tsparser.TypeScriptParser;
import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.TypeScriptParserBaseListener;

public class TypeScriptAssertionContext extends TypeScriptParserBaseListener {

    private final TypeScriptParser parser;

    private final Map<Class<? extends ParserRuleContext>, List<ParserRuleContext>> rules;
    private final List<ErrorNode> errors;

    public TypeScriptAssertionContext(TypeScriptParser parser) {
        this.parser = parser;
        this.rules = new HashMap<>();
        this.errors = new ArrayList<>();
    }

    public TypeScriptParser getParser() {
        return parser;
    }

    public List<ErrorNode> getErrors() {
        return errors;
    }

    public Map<Class<? extends ParserRuleContext>, List<ParserRuleContext>> getRules() {
        return rules;
    }

    public <T extends ParserRuleContext> List<T> getRules(Class<T> ruleType) {
        return (List<T>) rules.get(ruleType);
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        rules.computeIfAbsent(ctx.getClass(), k -> new ArrayList<>()).add(ctx);
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        errors.add(node);
    }

    public List<IdentifierNameContext> getIdentifierNames() {
        return getRules(IdentifierNameContext.class);
    }

    public List<IdentifierReferenceContext> getIdentifierReferences() {
        return getRules(IdentifierReferenceContext.class);
    }

    public List<IdentifierExpressionContext> getIdentifierExpressions() {
        return getRules(IdentifierExpressionContext.class);
    }

    public List<LiteralContext> getLiterals() {
        return getRules(LiteralContext.class);
    }

    public List<ArrayLiteralContext> getArrayLiterals() {
        return getRules(ArrayLiteralContext.class);
    }

}
