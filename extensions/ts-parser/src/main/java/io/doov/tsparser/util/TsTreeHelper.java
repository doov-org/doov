package io.doov.tsparser.util;

import java.util.ArrayList;
import java.util.List;

import io.doov.tsparser.TypeScriptParser;

public class TsTreeHelper {
    public static String getImportedModulePathOfImport(TypeScriptParser.ImportDeclarationContext ctx) {
        try {
            // string literal is like: 'path' but we only want: path.
            String importedModuleStringLiteral = ctx.fromClause().moduleSpecifier().getText();
            // cut off the ' ': note: substring's endIndex is exclusive
            return importedModuleStringLiteral.substring(1, importedModuleStringLiteral.length() - 1);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public static List<TypeScriptParser.ImplementationModuleElementContext> getTopLevelModuleElements(
                    TypeScriptParser.SourceFileContext sourceFile) {
        List<TypeScriptParser.ImplementationModuleElementContext> implementationElements;
        try {
            implementationElements = sourceFile.implementationSourceFile().implementationModule()
                            .implementationModuleElement();
        } catch (NullPointerException npe) {
            return new ArrayList<>();
        }

        return implementationElements != null ? implementationElements : new ArrayList<>();
    }

    public static TypeScriptParser.VariableStatementContext getExportVariableStatement(
                    TypeScriptParser.ImplementationModuleElementContext moduleElement) {
        try {
            // returns null, if it is an export element but not a varialbe statement:
            return moduleElement.exportImplementationElement().variableStatement();
        } catch (NullPointerException npe) {
            // returns null, if it is not an export element:
            return null;
        }
    }

    public void getAllParameters(TypeScriptParser.ParameterListContext parameterList) {

    }

}
