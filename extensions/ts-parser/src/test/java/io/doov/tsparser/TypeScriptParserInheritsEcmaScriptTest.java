package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Test for {@link TypeScriptParser}, which is generated from the g4 grammar.
 *
 * <p>
 * Asserts that the TypeScriptParser also parses some basic JavaScript inputs. This is basically a regression test to
 * ensure that the TypeScriptParser is not completely broken or overwrites JavaScript behaviour.
 * </p>
 */
public class TypeScriptParserInheritsEcmaScriptTest {

    @Test
    public void testVariableStatements() {
        testVariableStatement("var");
        testVariableStatement("let");
        testVariableStatement("const");
    }

    private void testVariableStatement(String varModifier) {
        VariableStatementContext parsed = TsParserTestUtil.test(varModifier + " foobar = 42;",
                        TypeScriptParser::variableStatement);

        VariableDeclarationContext varDecl = parsed.variableDeclarationList().variableDeclaration(0);
        SimpleVariableDeclarationContext simpleVarDec = varDecl.simpleVariableDeclaration();
        assertEquals("foobar", simpleVarDec.bindingIdentifier().getText());
        assertEquals("42", simpleVarDec.initializer().singleExpression().getText());
    }

}
