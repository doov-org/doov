package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.AlternativeTestUtil;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests Property Definition Types to work within {@link TypeScriptParser#objectLiteral() object literals}
 */
public class TypeScriptParserA2PropertyDefinitionTest {

    @Test
    public void testPropertyShorthands() {
        ObjectLiteralContext parsed = TsParserTestUtil.test("{ a, b }", TypeScriptParser::objectLiteral);

        PropertyShorthandContext prop0 = AlternativeTestUtil.alt(parsed.propertyAssignment(0),
                        PropertyShorthandContext.class);
        PropertyShorthandContext prop1 = AlternativeTestUtil.alt(parsed.propertyAssignment(1),
                        PropertyShorthandContext.class);

        assertEquals("a", prop0.getText());
        assertEquals("b", prop1.getText());
    }

    @Test
    public void testCoverInitializedName() {
        ObjectLiteralContext parsed = TsParserTestUtil.test("{ a = 42 }", TypeScriptParser::objectLiteral);

        CoverInitializedNameContext prop0 = AlternativeTestUtil.alt(parsed.propertyAssignment(0),
                        CoverInitializedNameContext.class);

        // Test left side of "="
        assertEquals("a", prop0.propertyName().getText());

        // Test right side of "="
        LiteralContext assignementLiteral = AlternativeTestUtil
                        .alt(prop0.singleExpression(), LiteralExpressionContext.class).literal();
        assertEquals("42", assignementLiteral.getText());
    }

    // TODO: PropertyExpressionAssignment, MethodProperty

    @Test
    public void testGetterProperty() {
        ObjectLiteralContext parsed = TsParserTestUtil.test("{ get fooBar(): FooBarType { return this.fooBar; } }",
                        TypeScriptParser::objectLiteral);

        GetAccessorContext getter = AlternativeTestUtil.alt(parsed.propertyAssignment(0), PropertyGetterContext.class)
                        .getAccessor();
        assertEquals("fooBar", getter.propertyName().getText());
        assertEquals("FooBarType", getter.typeAnnotation().type().getText());
        assertEquals("returnthis.fooBar;", getter.functionBody().getText());
    }

    @Test
    public void testSetterProperty() {
        ObjectLiteralContext parsed = TsParserTestUtil.test(
                        "{ set fooBar(fooBarParam: FooBarType) { this.fooBar = fooBarParam; } }",
                        TypeScriptParser::objectLiteral);

        SetAccessorContext setter = AlternativeTestUtil.alt(parsed.propertyAssignment(0), PropertySetterContext.class)
                        .setAccessor();
        assertEquals("fooBar", setter.propertyName().getText());
        assertEquals("fooBarParam", setter.bindingIdentifierOrPattern().getText());
        assertEquals("FooBarType", setter.typeAnnotation().type().getText());
        assertEquals("this.fooBar=fooBarParam;", setter.functionBody().getText());
    }
}
