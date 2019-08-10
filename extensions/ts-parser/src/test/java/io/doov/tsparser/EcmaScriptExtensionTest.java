package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.JavaScriptParser.*;
import io.doov.tsparser.testutil.ESParserTestUtil;

/**
 * Test for {@link JavaScriptParser} which is generated from g4 grammer.
 * <p>
 * Tests those parts of the grammar which I had to add to complete the ES2015 spec.
 * </p>
 */
public class EcmaScriptExtensionTest {

    @Test
    public void testImportDeclaration() {
        ImportDeclarationContext parsed = ESParserTestUtil.test(
                        "import {Entity1, Entity2 as MyEntity2,} from '../../_jangbridge/model/';",
                        JavaScriptParser::importDeclaration);

        // Assert Entity1:
        ImportsListContext importsList = parsed.importClause().namedImports().importsList();
        ImportedBindingContext simpleImported = importsList.importSpecifier(0).importedBinding();
        assertEquals("Entity1", simpleImported.getText());

        // Assert Entity2 as MyEntity2:
        ImportSpecifierContext renamedImportSpec = importsList.importSpecifier(1);
        assertEquals("Entity2", renamedImportSpec.identifierName().getText());
        assertEquals("MyEntity2", renamedImportSpec.importedBinding().getText());
    }

    @Test
    public void testExportConstDeclaration() {
        ExportDeclarationContext parsed = ESParserTestUtil.test("export const TestService1_MyVariant = 42",
                        JavaScriptParser::exportDeclaration);

        assertNotNull(parsed.variableStatement().varModifier().Const());
        assertNull(parsed.variableStatement().varModifier().Var());
    }

    @Test
    public void testExportClassDeclaration() {
        ExportDeclarationContext parsed = ESParserTestUtil.test(
                        "export class FooBar {" + " doTheFoo() { console.log('baz'); }" + " }",
                        JavaScriptParser::exportDeclaration);

        assertNull(parsed.classDeclaration()); // this is the classDeclaration from rule 'export default ...'
        assertNotNull(parsed.declaration().classDeclaration()); // and this is the correct declaration
        assertEquals("console.log('baz');",
                        parsed.declaration().classDeclaration().classTail().classBody().classElement(0)
                                        .methodDefinition().functionBody().sourceElements().sourceElement(0).statement()
                                        .getText());
    }

    @Test
    public void testPropertyDefinition() {
        PropertyAssignmentContext parsed = ESParserTestUtil.test("foo = asdf", JavaScriptParser::propertyAssignment);

        assertTrue(parsed instanceof PropertyExpressionAssignmentContext);
        assertEquals("foo", ((PropertyExpressionAssignmentContext) parsed).propertyName().getText());
    }
}
