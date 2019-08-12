package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.TypeContext;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests {@link TypeScriptParser#type()} for Constructor Types
 */
public class TypeScriptParserA1ConstructorTypeTest {
    /**
     * Parameter Lists are tested in {@link TypeScriptParserA1FunctionTypesTest}, so they are blackboxed here.
     */
    @Test
    public void testFunctionTypeMixedParams() {
        TypeContext parsed = TsParserTestUtil.test(
                        "new <Foo, Bar> (private r: string, public o: number = 42, o2?, ...args: string) => FooBar",
                        TypeScriptParser::type);

        assertEquals("<Foo,Bar>", parsed.constructorType().typeParameters().getText());
        assertEquals("private r: string, public o: number = 42, o2?, ...args: string".replace(" ", ""),
                        parsed.constructorType().parameterList().getText());
        assertEquals("FooBar", parsed.constructorType().type().getText());
    }

}
