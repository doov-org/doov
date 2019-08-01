package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.AlternativeTestUtil;
import io.doov.tsparser.testutil.TsParserTestUtil;

public class TypeScriptParserMiscTest {

    @Test
    public void testCreateTypeInstance() {
        SingleExpressionContext parsed = TsParserTestUtil.test(
                        "new InjectionToken<TestService1>('Contains all fields for AppComponent')",
                        TypeScriptParser::singleExpression);

        NewExpressionContext newExpression = AlternativeTestUtil.alt(parsed, NewSingleExpressionContext.class)
                        .newExpression();

        assertEquals("InjectionToken", newExpression.identifierReference().getText());
        assertEquals("<TestService1>", newExpression.arguments().typeArguments().getText());
        assertNotNull(newExpression.arguments());
    }

    @Test
    public void testGenericMethodCall() {
        SingleExpressionContext parsed = TsParserTestUtil.test("foo<Entity1[]>('bar')",
                        TypeScriptParser::singleExpression);

        CallExpressionContext callExpression = AlternativeTestUtil.alt(parsed, CallSingleExpressionContext.class)
                        .callExpression();

        assertEquals("foo", callExpression.memberExpression().primaryExpression().identifierReference().getText());
        TypeContext type = callExpression.arguments().typeArguments().typeArgumentList().typeArgument(0).type();
        assertEquals("Entity1[]", type.getText());

        PrimaryTypeContext primary = AlternativeTestUtil
                        .alt(type.unionOrIntersectionOrPrimaryType(), PrimaryContext.class).primaryType();
        PrimaryTypeContext wrappedType = AlternativeTestUtil.alt(primary, ArrayPrimTypeContext.class).primaryType();
        assertEquals("Entity1", wrappedType.getText());

        assertEquals("'bar'", callExpression.arguments().argumentsList().getText());
    }

}
