package io.doov.tsparser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.*;
import io.doov.tsparser.testutil.AlternativeTestUtil;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Tests Interface Declarations.
 * <p>
 * The 'objectType' rule (the interface's body) is already tested in
 * {@link TypeScriptParserA1PrimaryTypesObjectTypeTest}, so they are blackboxed here.
 * </p>
 */
public class TypeScriptParserExt2DecoratorTest {

    @Test
    public void testDecoratorList() {
        DecoratorListContext parsed = TsParserTestUtil.test("@Component({selector: 'app-root',\n"
                        + "  templateUrl: './app.component.html',\n" + "  styleUrls: ['./app.component.css']})",
                        TypeScriptParser::decoratorList);
        DecoratorCallExpressionContext decoratorCall = parsed.decorator(0).decoratorCallExpression();

        assertEquals("Component", decoratorCall.decoratorMemberExpression().identifierReference().getText());

        // assert first argument is parsed as object literal:
        AlternativeTestUtil.alt(decoratorCall.arguments().argumentsList().singleExpression(0),
                        ObjectLiteralExpressionContext.class);
    }

    @Test
    public void testClassDecorator() {
        ClassDeclarationContext parsed = TsParserTestUtil.test("@Component(42)" + "class FooBar {}",
                        TypeScriptParser::classDeclaration);

        ClassDeclarationContext classDeclaration = parsed;// parsed.classDeclaration();
        DecoratorCallExpressionContext decorator = classDeclaration.decoratorList().decorator(0)
                        .decoratorCallExpression();

        assertEquals("Component", decorator.decoratorMemberExpression().identifierReference().getText());
    }

    @Test
    public void testExportClassDecorator() {
        ExportImplementationElementContext parsed = TsParserTestUtil.test("@Component(42)" + "export class FooBar {}",
                        TypeScriptParser::exportImplementationElement);

        // ClassDeclarationContext classDeclaration = parsed.classDeclaration();
        // watch out, decorator does not belong to parsed.classDeclaration(), but to the exportImplElement itself:
        DecoratorCallExpressionContext decorator = parsed.decoratorList().decorator(0).decoratorCallExpression();

        assertEquals("Component", decorator.decoratorMemberExpression().identifierReference().getText());
    }

}
