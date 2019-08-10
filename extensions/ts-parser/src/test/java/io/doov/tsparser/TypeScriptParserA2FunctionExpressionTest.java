package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.AlternativeTestUtil;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests the Function Expression and Arrow Function Parameters Type.
 * <p>
 * Call Signatures are already tested in {@link TypeScriptParserA1PrimaryTypesObjectTypeTest} and
 * {@link TypeScriptParserA1FunctionTypesTest}, so they are blackboxed here.
 * </p>
 */
public class TypeScriptParserA2FunctionExpressionTest {

    @Test
    public void testFunctionAsSingleExpression() {
        SingleExpressionContext parsed = TsParserTestUtil.test(
                        "function doTheFoo <T, E> (r: T, o?: E, ...args: string): number {" + "   console.log('Foo');"
                                        + "   return 42;" + "}",
                        TypeScriptParser::singleExpression);

        FunctionExpressionContext functionExpression = AlternativeTestUtil
                        .alt(parsed, FunctionSingleExpressionContext.class).functionExpression();

        assertEquals("doTheFoo", functionExpression.bindingIdentifier().getText());
        assertEquals("<T, E> (r: T, o?: E, ...args: string): number ".replace(" ", ""),
                        functionExpression.callSignature().getText());
        assertEquals("console.log('Foo');return42;", functionExpression.functionBody().getText());
    }

    @Test
    public void testArrowFunctionAsSingleExpression() {
        SingleExpressionContext parsed = TsParserTestUtil.test(
                        "<T, E> (r: T, o?: E, ...args: string): number => args.length();",
                        TypeScriptParser::singleExpression);

        ArrowFunctionExpressionContext arrowFunction = AlternativeTestUtil.alt(parsed,
                        ArrowFunctionExpressionContext.class);

        assertEquals("<T, E> (r: T, o?: E, ...args: string): number ".replace(" ", ""),
                        arrowFunction.arrowFunctionParameters().callSignature().getText());
        assertEquals("args.length()", arrowFunction.arrowFunctionBody().singleExpression().getText());
    }

}
