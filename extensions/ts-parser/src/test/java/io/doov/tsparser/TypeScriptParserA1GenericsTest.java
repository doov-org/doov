package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.AlternativeTestUtil;
import io.doov.tsparser.testutil.TsParserTestUtil;

public class TypeScriptParserA1GenericsTest {

    @Test
    public void testTypeParameters() {
        TypeParametersContext parsed = TsParserTestUtil.test("<Foo1, Baz2 extends Bar, Foobar>",
                        TypeScriptParser::typeParameters);

        TypeParameterContext type0 = parsed.typeParameterList().typeParameter(0);
        TypeParameterContext type1Construct = parsed.typeParameterList().typeParameter(1);
        BindingIdentifierContext type1 = type1Construct.bindingIdentifier();
        TypeContext type1ConstraintType = type1Construct.constraint().type();
        TypeParameterContext type2 = parsed.typeParameterList().typeParameter(2);

        assertEquals("Foo1", type0.getText());
        assertEquals("Baz2", type1.getText());
        assertEquals("Bar", type1ConstraintType.getText());
        assertEquals("Foobar", type2.getText());
    }

    @Test
    public void testTypeArguments() {
        TypeArgumentsContext parsed = TsParserTestUtil.test("<Foo, Bar>", TypeScriptParser::typeArguments);

        TypeArgumentContext type0 = parsed.typeArgumentList().typeArgument(0);
        TypeArgumentContext type1 = parsed.typeArgumentList().typeArgument(1);

        assertEquals("Foo", type0.getText());
        assertEquals("Bar", type1.getText());
    }

    @Test
    public void testTypeArgumentsDoNotAllowConstraints() {
        TypeArgumentsContext parsed = TsParserTestUtil.expectParseError("<Foo, Baz extends Baz>",
                        TypeScriptParser::typeArguments);
        assertNotNull(parsed);
    }

    @Test
    public void testTypeArrayAsTypeArgument() {
        TypeArgumentsContext parsed = TsParserTestUtil.test("<Foo[]>", TypeScriptParser::typeArguments);

        UnionOrIntersectionOrPrimaryTypeContext uipType = parsed.typeArgumentList().typeArgument(0).type()
                        .unionOrIntersectionOrPrimaryType();
        PrimaryTypeContext primaryType = AlternativeTestUtil.alt(uipType, PrimaryContext.class).primaryType();
        PrimaryTypeContext wrappedType = AlternativeTestUtil.alt(primaryType, ArrayPrimTypeContext.class).primaryType();

        assertEquals("Foo", wrappedType.getText());
    }
}
