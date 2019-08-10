package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests {@link TypeScriptParser#type()} for function types. This includes testing Parameter Lists.
 */
public class TypeScriptParserA1FunctionTypesTest {

    @Test
    public void testFunctionTypeWithoutParams() {
        TypeContext parsed = TsParserTestUtil.test("<T1, T2> () => Foobar", TypeScriptParser::type);

        assertEquals("<T1,T2>", parsed.functionType().typeParameters().getText());
        assertNull(parsed.functionType().parameterList());
        assertEquals("Foobar", parsed.functionType().type().getText());
    }

    @Test
    public void testFunctionTypeOnlyRequiredParams() {
        TypeContext parsed = TsParserTestUtil.test(
                        "(r1: number, private r2: string, noType, litType: \"Foobar\") => void",
                        TypeScriptParser::type);

        assertNull(parsed.functionType().typeParameters());
        assertEquals("void", parsed.functionType().type().getText());

        assertNull(parsed.functionType().parameterList().optionalParameterList());
        assertNull(parsed.functionType().parameterList().restParameter());

        // Params:
        RequiredParameterListContext requiredParams = parsed.functionType().parameterList().requiredParameterList();

        assertNull(requiredParams.requiredParameter(0).accessibilityModifier());
        assertEquals("r1:number", requiredParams.requiredParameter(0).getText());

        RequiredParameterContext r2 = requiredParams.requiredParameter(1);
        assertEquals("private", r2.accessibilityModifier().getText());
        assertEquals("r2", r2.bindingIdentifierOrPattern().getText());
        assertEquals("string", r2.typeAnnotation().type().getText());

        RequiredParameterContext noType = requiredParams.requiredParameter(2);
        assertNull(noType.accessibilityModifier());
        assertEquals("noType", noType.bindingIdentifierOrPattern().getText());
        assertNull(noType.typeAnnotation());

        RequiredParameterContext litType = requiredParams.requiredParameter(3);
        assertEquals("\"Foobar\"", litType.StringLiteral().getText());
    }

    @Test
    public void testFunctionTypeOnlyOptionalParams() {
        TypeContext parsed = TsParserTestUtil.test("(o1?, private o2: number = 42, o3 = 'foobar') => void",
                        TypeScriptParser::type);

        assertNull(parsed.functionType().typeParameters());
        assertEquals("void", parsed.functionType().type().getText());

        assertNull(parsed.functionType().parameterList().requiredParameterList());
        assertNull(parsed.functionType().parameterList().restParameter());

        // Params:
        OptionalParameterListContext optionalParams = parsed.functionType().parameterList().optionalParameterList();

        assertNull(optionalParams.optionalParameter(0).accessibilityModifier());
        assertEquals("o1", optionalParams.optionalParameter(0).bindingIdentifierOrPattern().getText());

        OptionalParameterContext o2 = optionalParams.optionalParameter(1);
        assertEquals("private", o2.accessibilityModifier().getText());
        assertEquals("o2", o2.bindingIdentifierOrPattern().getText());
        assertEquals("number", o2.typeAnnotation().type().getText());
        assertEquals("42", o2.initializer().singleExpression().getText());

        OptionalParameterContext o3 = optionalParams.optionalParameter(2);
        assertEquals("o3", o3.bindingIdentifierOrPattern().getText());
        assertEquals("'foobar'", o3.initializer().singleExpression().getText());
    }

    @Test
    public void testFunctionTypeOnlyRestParams() {
        TypeContext parsed = TsParserTestUtil.test("(... args: Foobar) => void", TypeScriptParser::type);

        assertNull(parsed.functionType().typeParameters());
        assertEquals("void", parsed.functionType().type().getText());

        assertNull(parsed.functionType().parameterList().requiredParameterList());
        assertNull(parsed.functionType().parameterList().optionalParameterList());

        // Params:
        RestParameterContext restParam = parsed.functionType().parameterList().restParameter();

        assertEquals("args", restParam.bindingIdentifier().getText());
        assertEquals("Foobar", restParam.typeAnnotation().type().getText());
    }

    @Test
    public void testFunctionTypeMixedParams() {
        TypeContext parsed = TsParserTestUtil.test(
                        "(private r: number, public o: string = 'foobar', ... args: Foobar) => void",
                        TypeScriptParser::type);

        assertNull(parsed.functionType().typeParameters());
        assertEquals("void", parsed.functionType().type().getText());

        // Requried Params:
        RequiredParameterListContext requiredParams = parsed.functionType().parameterList().requiredParameterList();

        assertEquals("private r: number".replace(" ", ""), requiredParams.requiredParameter(0).getText());

        // Optional Params:
        OptionalParameterListContext optionalParams = parsed.functionType().parameterList().optionalParameterList();

        assertEquals("public o: string = 'foobar'".replace(" ", ""), optionalParams.optionalParameter(0).getText());

        // Rest Params:
        RestParameterContext restParam = parsed.functionType().parameterList().restParameter();

        assertEquals("args", restParam.bindingIdentifier().getText());
        assertEquals("Foobar", restParam.typeAnnotation().type().getText());
    }

}
