package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests the different kinds of declaration rules that have been overriden by TypeScript grammar.
 *
 * <ul>
 * <li>Tests the added Declarations alternatives: Interface(yet to do), Type Alias and Enum(yet to do) Declarations.
 * </li>
 * <li>Tests variable declarations.</li>
 * </ul>
 */
public class TypeScriptParserA3DeclarationTest {

    @Test
    public void testTypeAliasDeclaration() {
        StatementListItemContext parsed = TsParserTestUtil.test("type foo<Bar, Baz> = {bar: Bar; baz: Baz};",
                        TypeScriptParser::statementListItem);
        TypeAliasDeclarationContext aliasDeclaration = parsed.declaration().typeAliasDeclaration();
        assertEquals("foo", aliasDeclaration.bindingIdentifier().getText());
        assertEquals("<Bar,Baz>", aliasDeclaration.typeParameters().getText());
        assertEquals("{bar: Bar; baz: Baz}".replace(" ", ""), aliasDeclaration.type().getText());
    }

    /**
     * Only testing destructutingVariableDeclaration, simpleVariableDeclaration is tested in
     * {@link TypeScriptParserInheritsEcmaScriptTest#testVariableStatement(String)} <br/>
     * <br/>
     * This method tests destructuring for arrays. <br/>
     */
    @Test
    public void testArrayDestructVarDeclaration() {
        VariableDeclarationContext parsed = TsParserTestUtil.test("[foo, bar] = ['foobar', 42]",
                        TypeScriptParser::variableDeclaration);
        assertEquals("[foo,bar]",
                        parsed.destructuringVariableDeclaration().bindingPattern().arrayBindingPattern().getText());
        assertEquals("['foobar',42]",
                        parsed.destructuringVariableDeclaration().initializer().singleExpression().getText());
    }

    /**
     * Only testing destructutingVariableDeclaration, simpleVariableDeclaration is tested in
     * {@link TypeScriptParserInheritsEcmaScriptTest#testVariableStatement(String)} <br/>
     * <br/>
     * This method tests destructuring for objects. <br/>
     */
    @Test
    public void testObjectDestructVarDeclaration() {
        VariableDeclarationContext parsed = TsParserTestUtil.test("{foo, bar = 4711} = {foo: 'foobar', bar: 42}", // 4711
                        TypeScriptParser::variableDeclaration);
        assertEquals("{foo,bar=4711}",
                        parsed.destructuringVariableDeclaration().bindingPattern().objectBindingPattern().getText());
        assertEquals("{foo:'foobar',bar:42}",
                        parsed.destructuringVariableDeclaration().initializer().singleExpression().getText());
    }

}
