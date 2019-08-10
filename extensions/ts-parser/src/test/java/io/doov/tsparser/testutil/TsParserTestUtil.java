package io.doov.tsparser.testutil;

import io.doov.tsparser.TypeScriptParser;
import io.doov.tsparser.util.TypeScriptParserFactory;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.ParserRuleContext;
import org.junit.jupiter.api.Assertions;

import static org.antlr.v4.runtime.CharStreams.fromStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TsParserTestUtil {

    public static TypeScriptParser parse(String input) {
        CharStream charStream = CharStreams.fromString(input);
        return TypeScriptParserFactory.parse(charStream);
    }

    public static TypeScriptParser parse(InputStream input) {
        CharStream charStream = null;
        try {
            charStream = fromStream(input);
        } catch (IOException e) {
            Assertions.fail("Could not read testdata", e);
        }
        return TypeScriptParserFactory.parse(charStream);
    }

    public static <T extends ParserRuleContext> T test(String input, Function<TypeScriptParser, T> ruleInvoker) {
        TypeScriptParser parser = parse(input);
        T result = ruleInvoker.apply(parser);

        assertEquals(0, parser.getNumberOfSyntaxErrors(), String.format(
                        "UNEXPECTED PARSE ERROR: The input '%s' should be parsed without syntax errors.", input));

        return result;
    }

    public static <T extends ParserRuleContext> T test(InputStream input, Function<TypeScriptParser, T> ruleInvoker) {
        TypeScriptParser parser = parse(input);
        T result = ruleInvoker.apply(parser);

        assertEquals(0, parser.getNumberOfSyntaxErrors(), String.format(
                        "UNEXPECTED PARSE ERROR: The input '%s' should be parsed without syntax errors.", input));

        return result;
    }

    public static <T extends ParserRuleContext> T expectParseError(String input,
                    Function<TypeScriptParser, T> ruleInvoker) {
        TypeScriptParser parser = parse(input);

        parser.removeErrorListeners();
        // do not spam the log with expected syntax errors
        // parser.addErrorListener(new BaseErrorListener() {
        // @Override
        // public void syntaxError(Recognizer<?, ?> recognizer,
        // Object offendingSymbol, int line, int charPositionInLine,
        // String msg, RecognitionException e) {
        // System.out.println(((Token) offendingSymbol).getText());
        // }
        // });

        T result = ruleInvoker.apply(parser);

        assertNotEquals(0, parser.getNumberOfSyntaxErrors(),
                        String.format("EXPECTED PARSE ERROR: The input '%s' should throw a syntax error,"
                                        + " but was considered valid by the parser.", input));

        return result;
    }

    public static <T extends ParserRuleContext> T expectParseError(InputStream input,
                    Function<TypeScriptParser, T> ruleInvoker) {
        TypeScriptParser parser = parse(input);

        parser.removeErrorListeners(); // do not spam the log with expected syntax errors

        T result = ruleInvoker.apply(parser);

        assertNotEquals(0, parser.getNumberOfSyntaxErrors(),
                        String.format("EXPECTED PARSE ERROR: The input '%s' should throw a syntax error,"
                                        + " but was considered valid by the parser.", input));

        return result;
    }

    public static <T extends ParserRuleContext> List<T> test(Function<TypeScriptParser, T> ruleInvoker,
                    String... inputs) {
        List<T> results = new ArrayList<>(inputs.length);
        for (String input : inputs) {
            results.add(test(input, ruleInvoker));
        }
        return results;
    }

}
