package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.FunctionDeclarationContext;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests the Function Declarations.
 * <p>
 * Call Signatures are already tested in {@link TypeScriptParserA1PrimaryTypesObjectTypeTest} and
 * {@link TypeScriptParserA1FunctionTypesTest}, so they are blackboxed here.
 * </p>
 */
public class TypeScriptParserA4FunctionDeclarationTest {

    @Test
    public void testEmptyFunctionBody() {
        FunctionDeclarationContext parsed = TsParserTestUtil.test(
                        "function doTheFoo <T, E> (r: T, o?: E, ...args: string): number;",
                        TypeScriptParser::functionDeclaration);

        assertEquals("doTheFoo", parsed.bindingIdentifier().getText());
        assertEquals("<T, E> (r: T, o?: E, ...args: string): number".replace(" ", ""),
                        parsed.callSignature().getText());
        assertNull(parsed.functionBody());
    }

    @Test
    public void testFunctionDeclaration() {
        FunctionDeclarationContext parsed = TsParserTestUtil
                        .test("function doTheFoo <T, E> (r: T, o?: E, ...args: string): number {" + "   console.log(r);"
                                        + "}", TypeScriptParser::functionDeclaration);

        assertEquals("doTheFoo", parsed.bindingIdentifier().getText());
        assertEquals("<T, E> (r: T, o?: E, ...args: string): number".replace(" ", ""),
                        parsed.callSignature().getText());
        assertEquals("console.log(r);", parsed.functionBody().getText());
    }

}
