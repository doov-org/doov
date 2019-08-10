package io.doov.tsparser.util;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;

import io.doov.tsparser.TypeScriptParser;

public class TsDebugHelper {
    public static void printTokens(Lexer lexer) {
        // TODO: this does not print the proper rule name
        System.out.println(lexer.getAllTokens().stream()
                        .map(o -> "(" + lexer.getRuleNames()[o.getType()] + ": \"" + o.getText() + "\")")
                        .collect(Collectors.toList()));
    }

    /**
     * Print the tree of the given context for TypeScript.
     * 
     * @param ctx context
     */
    public static void printTsTree(ParserRuleContext ctx) {
        if (ctx == null) {
            System.out.println("null");
            return;
        }

        String stringTree = ctx.toStringTree(Arrays.asList(TypeScriptParser.ruleNames));

        StringBuilder sb = new StringBuilder();
        int level = 0;
        for (char c : stringTree.toCharArray()) {
            if (c == ')') {
                sb.append('\n');
                level--;
                for (int i = 0; i < level; i++)
                    sb.append('\t');
            }

            sb.append(c);

            if (c == '(') {
                sb.append('\n');
                level++;
                for (int i = 0; i < level; i++)
                    sb.append('\t');
            }
        }

        System.out.println(sb.toString());
    }
}
