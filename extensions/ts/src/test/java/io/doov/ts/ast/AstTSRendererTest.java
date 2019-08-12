/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast;

import static org.assertj.core.api.Assertions.assertThat;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.RuleNode;
import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser;
import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.util.TypeScriptParserFactory;

class AstTSRendererTest {

    private final String import_stmt = "import * as DOOV from 'doov';";
    private final String mapping_stmt = "DOOV.map(field1, field2).using(converter).to(field3);";

    @Test
    void import_expression() {
        TypeScriptParser parser = TypeScriptParserFactory.parse(CharStreams.fromString(import_stmt));
        ImportDeclarationContext importDeclarationContext = parser.importDeclaration();
        System.out.println(importDeclarationContext.getText());
        System.out.println(importDeclarationContext.importClause().nameSpaceImport().importedBinding().getText());
        System.out.println(importDeclarationContext.fromClause().moduleSpecifier().getText());
    }

    @Test
    void mapping() {
        TypeScriptParser parser = TypeScriptParserFactory.parse(CharStreams.fromString(mapping_stmt));
        assertThat(parser.getNumberOfSyntaxErrors()).isEqualTo(0);
        CallExpressionContext callExpressionContext = parser.callExpression();
        System.out.println(callExpressionContext.getText());
        callExpressionContext.accept(new AbstractParseTreeVisitor<Object>() {
            @Override
            public Object visitChildren(RuleNode node) {
                if (node instanceof CallExpressionContext) {
                    CallExpressionContext call = (CallExpressionContext) node;
                    System.out.println(call.getText());
                    if (call.identifierName() != null) {
                        System.out.println(call.identifierName().getText());
                    }
                    if (call.memberExpression() != null) {
                        System.out.println(call.memberExpression().getText());
                    }
                }
                return super.visitChildren(node);
            }
        });
    }

    @Test
    void import_and_mapping() {
        String imp_and_mapping = import_stmt + "\n\n" + mapping_stmt;
        TypeScriptParser parser = TypeScriptParserFactory.parse(CharStreams.fromString(imp_and_mapping));
        assertThat(parser.getNumberOfSyntaxErrors()).isEqualTo(0);
        ImplementationModuleContext impl = parser.implementationSourceFile().implementationModule();
        System.out.println(impl.getText());
        System.out.println(impl.children.size());
        impl.children.forEach(c -> {
            ImplementationModuleElementContext moduleElement = (ImplementationModuleElementContext) c;
            ImplementationElementContext imp = moduleElement.implementationElement();
            ImportDeclarationContext importDeclarationContext = moduleElement.importDeclaration();
            if (importDeclarationContext != null) {
                System.out.println(importDeclarationContext.getText());
            }
            if (imp != null) {
                imp.children.forEach(i -> {
                    if (i instanceof StatementContext) {
                        StatementContext statementContext = (StatementContext) i;
                        ExpressionStatementContext expression = statementContext.expressionStatement();
                        expression.expressionSequence().singleExpression().forEach(e -> {
                            System.out.println(e.getText());
                        });
                        System.out.println(expression.getText());
                    }
                });
            }
        });
    }
}
