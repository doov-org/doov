package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.ClassOrInterfaceTypeListContext;
import io.doov.tsparser.TypeScriptParser.InterfaceDeclarationContext;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests Interface Declarations.
 * <p>
 * The 'objectType' rule (the interface's body) is already tested in
 * {@link TypeScriptParserA1PrimaryTypesObjectTypeTest}, so they are blackboxed here.
 * </p>
 */
public class TypeScriptParserA5InterfaceDeclarationTest {

    @Test
    public void testInterfaceDeclaration() {
        InterfaceDeclarationContext parsed = TsParserTestUtil.test(
                        "interface Foo<T1, T2> extends Bar<T1>, Baz<T2> {a: FooBar;}",
                        TypeScriptParser::interfaceDeclaration);

        assertEquals("Foo", parsed.bindingIdentifier().getText());
        assertEquals("<T1,T2>", parsed.typeParameters().getText());

        ClassOrInterfaceTypeListContext extendsList = parsed.interfaceExtendsClause().classOrInterfaceTypeList();
        assertEquals("Bar<T1>", extendsList.classOrInterfaceType(0).typeReference().getText());
        assertEquals("Baz<T2>", extendsList.classOrInterfaceType(1).typeReference().getText());

        assertEquals("a:FooBar;", parsed.objectType().typeBody().getText());
    }

}
