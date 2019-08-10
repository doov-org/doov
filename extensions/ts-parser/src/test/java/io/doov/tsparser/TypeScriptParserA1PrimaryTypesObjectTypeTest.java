package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests {@link TypeScriptParser#type()} for the Primary Type "ObjectType".<br/>
 * <b>Basically this is also a test for INTERFACE BODIES</b>, as interfaces are kind of a "named objecttype". Thus,
 * ObjectTypes can get rather complex and the test fills its own class.
 */
public class TypeScriptParserA1PrimaryTypesObjectTypeTest {

    @Test
    public void testEmtpyObjectType() {
        TypeContext parsed = TsParserTestUtil.test("{}", TypeScriptParser::type);

        ObjectTypeContext objectType = getObjectTypeContext(parsed);

        assertNull(objectType.typeBody());
    }

    @Test
    public void testPropertySignatures() {
        TypeContext parsed = TsParserTestUtil.test("{field1: number, field2?: string; field3: Foobar<Baz>}",
                        TypeScriptParser::type);

        ObjectTypeContext objectType = getObjectTypeContext(parsed);
        TypeMemberListContext members = objectType.typeBody().typeMemberList();

        PropertySignaturContext member0 = members.typeMember(0).propertySignatur();
        assertEquals("field1", member0.propertyName().getText());
        assertEquals("number", member0.typeAnnotation().type().getText());

        PropertySignaturContext member1 = members.typeMember(1).propertySignatur();
        assertEquals("field2", member1.propertyName().getText());
        assertEquals("string", member1.typeAnnotation().type().getText());

        PropertySignaturContext member2 = members.typeMember(2).propertySignatur();
        assertEquals("field3", member2.propertyName().getText());
        assertEquals("Foobar<Baz>", member2.typeAnnotation().type().getText());
    }

    @Test
    public void testEmptyCallSignatures() {
        TypeContext parsed = TsParserTestUtil.test("{()}", TypeScriptParser::type);

        ObjectTypeContext objectType = getObjectTypeContext(parsed);
        CallSignatureContext callSignature = objectType.typeBody().typeMemberList().typeMember(0).callSignature();

        assertNull(callSignature.typeParameters());
        assertNull(callSignature.parameterList());
        assertNull(callSignature.typeAnnotation());
    }

    /**
     * parameterlists are tested in detail in {@link TypeScriptParserA1FunctionTypesTest}. So they are blackboxed in
     * this test.
     */
    @Test
    public void testCallSignatures() {
        TypeContext parsed = TsParserTestUtil.test("{<Foo, Bar> (str: string, foo: Foo): Bar}", TypeScriptParser::type);

        ObjectTypeContext objectType = getObjectTypeContext(parsed);
        CallSignatureContext callSignature = objectType.typeBody().typeMemberList().typeMember(0).callSignature();

        assertEquals("<Foo,Bar>", callSignature.typeParameters().getText());
        assertEquals("str: string, foo: Foo".replace(" ", ""), callSignature.parameterList().getText());
        assertEquals("Bar", callSignature.typeAnnotation().type().getText());
    }

    @Test
    public void testEmptyConstructSignatures() {
        TypeContext parsed = TsParserTestUtil.test("{new ()}", TypeScriptParser::type);

        ObjectTypeContext objectType = getObjectTypeContext(parsed);
        ConstructSignatureContext callSignature = objectType.typeBody().typeMemberList().typeMember(0)
                        .constructSignature();

        assertNull(callSignature.typeParameters());
        assertNull(callSignature.parameterList());
        assertNull(callSignature.typeAnnotation());
    }

    /**
     * parameterlists are tested in detail in {@link TypeScriptParserA1FunctionTypesTest}. So they are blackboxed in
     * this test.
     */
    @Test
    public void testConstructSignatures() {
        TypeContext parsed = TsParserTestUtil.test("{new <Foo, Bar> (str: string, foo: Foo): Bar}",
                        TypeScriptParser::type);

        ObjectTypeContext objectType = getObjectTypeContext(parsed);
        ConstructSignatureContext callSignature = objectType.typeBody().typeMemberList().typeMember(0)
                        .constructSignature();

        assertEquals("<Foo,Bar>", callSignature.typeParameters().getText());
        assertEquals("str: string, foo: Foo".replace(" ", ""), callSignature.parameterList().getText());
        assertEquals("Bar", callSignature.typeAnnotation().type().getText());
    }

    /**
     * parameterlists are tested in detail in {@link TypeScriptParserA1FunctionTypesTest}. So they are blackboxed in
     * this test.
     */
    @Test
    public void testIndexSignatureString() {
        TypeContext parsed = TsParserTestUtil.test("{ [key: string]: {x: number, y: Foobar} }", TypeScriptParser::type);

        ObjectTypeContext objectType = getObjectTypeContext(parsed);
        IndexSignatureContext indexSignature = objectType.typeBody().typeMemberList().typeMember(0).indexSignature();

        assertEquals("key", indexSignature.bindingIdentifier().getText());
        assertEquals("{x: number, y: Foobar}".replace(" ", ""), indexSignature.typeAnnotation().type().getText());
    }

    /**
     * A Method Signature is like a "named Call Signature".<br/>
     * Call Signatures are already tested in {@link #testCallSignatures()}, so they are blackboxed here.
     */
    @Test
    public void testMethodSignatures() {
        TypeContext parsed = TsParserTestUtil.test("{ doTheFoo ? <Foo, Bar> (str: string, foo: Foo): Bar }",
                        TypeScriptParser::type);

        ObjectTypeContext objectType = getObjectTypeContext(parsed);
        MethodSignatureContext methodSignature = objectType.typeBody().typeMemberList().typeMember(0).methodSignature();

        assertEquals("doTheFoo", methodSignature.propertyName().getText());
        assertEquals("<Foo, Bar> (str: string, foo: Foo): Bar".replace(" ", ""),
                        methodSignature.callSignature().getText());
    }

    /**
     * Example from the TypeScript 1.8 Language Spec, p. 52
     */
    @Test
    public void testDifferentFunctionSignatures() {
        TypeContext parsed = TsParserTestUtil.test("{ " + "   func1(x: number): number;" + // Method
                                                                                           // signature
                        "   func2: (x: number) => number;" + // Function type literal
                        "   func3: { (x: number): number };" + // object type literal
                        " }", TypeScriptParser::type);

        ObjectTypeContext objectType = getObjectTypeContext(parsed);
        TypeMemberListContext members = objectType.typeBody().typeMemberList();

        assertEquals("func1(x: number): number".replace(" ", ""), members.typeMember(0).methodSignature().getText());

        assertEquals("(x: number) => number".replace(" ", ""),
                        members.typeMember(1).propertySignatur().typeAnnotation().type().functionType().getText());

        ObjectTypeContext nestedObjectType = getObjectTypeContext(
                        members.typeMember(2).propertySignatur().typeAnnotation().type());
        assertEquals("(x: number): number".replace(" ", ""),
                        nestedObjectType.typeBody().typeMemberList().typeMember(0).callSignature().getText());

    }

    //
    // Helpers:
    //

    private ObjectTypeContext getObjectTypeContext(TypeContext parsed) {
        UnionOrIntersectionOrPrimaryTypeContext uipType = parsed.unionOrIntersectionOrPrimaryType();

        assertTrue(uipType instanceof PrimaryContext);
        PrimaryTypeContext primaryType = ((PrimaryContext) uipType).primaryType();

        assertTrue(primaryType instanceof ObjectPrimTypeContext);
        return ((ObjectPrimTypeContext) primaryType).objectType();
    }

}
