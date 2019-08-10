package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests {@link TypeScriptParser#type()} for all Primary Types except for ObjectType.
 */
public class TypeScriptParserA1PrimaryTypesSimpleTypesTest {

    @Test
    public void testPredefinedTypes() {
        List<TypeContext> parsed = TsParserTestUtil.test(TypeScriptParser::type, "any", "number", "boolean", "string",
                        "symbol", "void");
    }

    @Test
    public void testTypeReference() {
        TypeContext parsed = TsParserTestUtil.test("de.f_estival.Foobar<Arg1,Arg2>", TypeScriptParser::type);

        PrimaryTypeContext primaryType = getPrimaryTypeContext(parsed);

        assertTrue(primaryType instanceof ReferencePrimTypeContext);
        TypeReferenceContext typeRef = ((ReferencePrimTypeContext) primaryType).typeReference();
        TypeNameContext typeName = typeRef.typeName();

        assertEquals("de.f_estival", typeName.namespaceName().getText());
        assertEquals("Foobar", typeName.identifierReference().getText());
        assertEquals("<Arg1,Arg2>", typeRef.typeArguments().getText());
    }

    @Test
    public void testParenthesizedTypes() {
        TypeContext parsed = TsParserTestUtil.test("(de.f_estival.Foobar)", TypeScriptParser::type);

        PrimaryTypeContext primaryType = getPrimaryTypeContext(parsed);

        assertTrue(primaryType instanceof ParenthesizedPrimTypeContext);
        TypeContext wrappedType = ((ParenthesizedPrimTypeContext) primaryType).type();

        assertEquals("de.f_estival.Foobar", wrappedType.getText());
    }

    // /**
    // * TODO: does not work yet!
    // */
    // @Test
    // public void testTypeReferenceAllowsNoLineTerminatorsBeforeTypeArguments() {
    // TypeContext parsed = TsParserTestUtil.expectParseError(
    // "de.f_estival.Foobar \n <Arg1,Arg2>",
    // TypeScriptParser::type
    // );
    // }

    @Test
    public void testArrayTypes() {
        TypeContext parsed = TsParserTestUtil.test("Foobar[][]", TypeScriptParser::type);

        PrimaryTypeContext outerPrimType = getPrimaryTypeContext(parsed);

        // unwrap first level of array:
        assertTrue(outerPrimType instanceof ArrayPrimTypeContext);
        PrimaryTypeContext firstWrap = ((ArrayPrimTypeContext) outerPrimType).primaryType();

        // unwrap second level of array:
        assertTrue(firstWrap instanceof ArrayPrimTypeContext);
        PrimaryTypeContext secondWrap = ((ArrayPrimTypeContext) firstWrap).primaryType();

        assertEquals("Foobar", secondWrap.getText());
    }

    @Test
    public void testTupleTypes() {
        TypeContext parsed = TsParserTestUtil.test("[number, de.f_estival.Foobar, string | boolean]",
                        TypeScriptParser::type);

        PrimaryTypeContext outerPrimType = getPrimaryTypeContext(parsed);

        // unwrap first level of array:
        assertTrue(outerPrimType instanceof TuplePrimTypeContext);
        TupleElementTypesContext tupleType = ((TuplePrimTypeContext) outerPrimType).tupleElementTypes();

        // unwrap second level of array:
        PrimaryTypeContext type0 = getPrimaryTypeContext(tupleType.tupleElementType(0).type());
        PrimaryTypeContext type1 = getPrimaryTypeContext(tupleType.tupleElementType(1).type());
        UnionOrIntersectionOrPrimaryTypeContext type2 = tupleType.tupleElementType(2).type()
                        .unionOrIntersectionOrPrimaryType(); // Union Type

        assertEquals("number", type0.getText());
        assertEquals("de.f_estival.Foobar", type1.getText());
        assertEquals("string|boolean", type2.getText());
        assertTrue(type2 instanceof UnionContext);
    }

    @Test
    public void testTypeQuery() {
        TypeContext parsed = TsParserTestUtil.test("typeof Foobar.barfoo.field1", TypeScriptParser::type);

        PrimaryTypeContext primType = getPrimaryTypeContext(parsed);
        assertTrue(primType instanceof QueryPrimTypeContext);
        TypeQueryExpressionContext typeQuery = ((QueryPrimTypeContext) primType).typeQuery().typeQueryExpression();

        assertEquals("Foobar", typeQuery.identifierName(0).getText());
        assertEquals("barfoo", typeQuery.identifierName(1).getText());
        assertEquals("field1", typeQuery.identifierName(2).getText());
    }

    @Test
    public void testThisType() {
        TypeContext parsed = TsParserTestUtil.test("this", TypeScriptParser::type);

        PrimaryTypeContext primType = getPrimaryTypeContext(parsed);
        assertTrue(primType instanceof ThisPrimTypeContext);
        ThisTypeContext thisType = ((ThisPrimTypeContext) primType).thisType();

        assertEquals("this", thisType.getText());
    }

    //
    // Helpers:
    //

    private PrimaryTypeContext getPrimaryTypeContext(TypeContext parsed) {
        UnionOrIntersectionOrPrimaryTypeContext uipType = parsed.unionOrIntersectionOrPrimaryType();
        assertTrue(uipType instanceof PrimaryContext);
        return ((PrimaryContext) uipType).primaryType();
    }

}
