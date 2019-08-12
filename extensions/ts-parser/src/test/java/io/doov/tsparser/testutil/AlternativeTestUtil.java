package io.doov.tsparser.testutil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.antlr.v4.runtime.ParserRuleContext;

public class AlternativeTestUtil {

    /**
     * Asserts, that the given RuleContext "parsed" is of type "targetClass" and also returns it properly casted.
     */
    public static <T extends ParserRuleContext> T alt(ParserRuleContext parsed, Class<T> targetClass) {
        assertEquals(targetClass, parsed.getClass());
        return ((T) parsed);
    }
}
