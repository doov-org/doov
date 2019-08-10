package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.AlternativeTestUtil;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests
 * <ul>
 * <li>the Unary Type Cast Expression.</li>
 * <li>arguments</li>
 * </ul>
 */
public class TypeScriptParserA2MiscTest {

    @Test
    public void testCastExpression() {
        SingleExpressionContext parsed = TsParserTestUtil.test("<FooBar> myVar;", TypeScriptParser::singleExpression);
        UnaryExpressionContext unaryExpression = AlternativeTestUtil.alt(parsed, UnarySingleExpressionContext.class)
                        .unaryExpression();
        TypeCastExpressionContext castExpression = AlternativeTestUtil.alt(unaryExpression,
                        TypeCastExpressionContext.class);

        assertEquals("FooBar", castExpression.type().getText());
        assertEquals("myVar", castExpression.singleExpression().getText());
    }

    @Test
    public void testArguments() {
        ArgumentsContext parsed = TsParserTestUtil.test("<Foo, Bar> ('foo', 42, null);", TypeScriptParser::arguments);

        assertEquals("<Foo,Bar>", parsed.typeArguments().getText());
        assertEquals("'foo',42,null", parsed.argumentsList().getText());
    }

}
