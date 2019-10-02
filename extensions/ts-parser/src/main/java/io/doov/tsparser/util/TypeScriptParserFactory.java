package io.doov.tsparser.util;

import java.io.*;
import java.util.function.Function;

import org.antlr.v4.runtime.*;

import io.doov.tsparser.*;

public class TypeScriptParserFactory {

    public static TypeScriptParser parse(CharStream charStream) {
        /*
         * Always parsing TS code as 'strict' mode. This means: the JavaScript Lexer parts that we inherit in TypeScript
         * Parser will treat TS code like strict JavaScript Code. Keywords like 'let', 'private' and many more are
         * currently only considered keywords in strict mode by the JavaScript Lexer.
         */
        TypeScriptLexer lexer = new TypeScriptLexer(charStream);
        lexer.setUseStrictDefault(true);

        // TsDebugHelper.printTokens(lexer);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        TypeScriptParser parser = new TypeScriptParser(tokens);

        return parser;
    }

    public static TypeScriptParser parse(InputStream input) throws IOException {
        return parse(CharStreams.fromStream(input));
    }

    public static TypeScriptParser parse(String input) throws IOException {
        return parse(CharStreams.fromStream(new ByteArrayInputStream(input.getBytes())));
    }

    public static <T extends TypeScriptParserListener> T parseUsing(String input,
            Function<TypeScriptParser, T> parserListenerProvider) throws IOException {
        return parserListenerProvider.apply(parse(input));
    }
}
