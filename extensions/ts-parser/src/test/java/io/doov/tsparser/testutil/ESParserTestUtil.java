package io.doov.tsparser.testutil;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.Function;

import org.antlr.v4.runtime.*;

import io.doov.tsparser.JavaScriptLexer;
import io.doov.tsparser.JavaScriptParser;

public class ESParserTestUtil {

    public static JavaScriptParser parse(String input) {
        CharStream charStream = CharStreams.fromString(input);
        JavaScriptLexer lexer = new JavaScriptLexer(charStream);
        lexer.setUseStrictDefault(true);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaScriptParser parser = new JavaScriptParser(tokens);

        return parser;
    }

    public static <T extends ParserRuleContext> T test(String input, Function<JavaScriptParser, T> ruleInvoker) {
        JavaScriptParser parser = parse(input);
        T result = ruleInvoker.apply(parser);

        assertEquals(0, parser.getNumberOfSyntaxErrors(), format(
                        "UNEXPECTED PARSE ERROR: The input '%s' should be parsed without syntax errors.", input));

        return result;
    }
}
