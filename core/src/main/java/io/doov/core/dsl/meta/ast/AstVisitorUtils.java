package io.doov.core.dsl.meta.ast;

import io.doov.core.dsl.meta.SyntaxTree;

public class AstVisitorUtils {

    public static String astToString(SyntaxTree syntaxTree) {
        StringBuilder stringBuilder = new StringBuilder();
        syntaxTree.accept(new AstLineVisitor(stringBuilder));
        return stringBuilder.toString();
    }

}
