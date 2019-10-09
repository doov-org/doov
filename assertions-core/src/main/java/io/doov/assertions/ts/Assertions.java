/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.assertions.ts;

import org.assertj.core.api.IntegerAssert;

public class Assertions {

    public static TypeScriptAssert assertThat(TypeScriptAssertionContext context) {
        return new TypeScriptAssert(context, TypeScriptAssert.class);
    }

    public static void assertParenthesis(String typeScriptText) {
        int p = 0;
        char[] chars = typeScriptText.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(') {
                p++;
            }
            if (chars[i] == ')') {
                p--;
            }
            new IntegerAssert(p).isNotNegative()
                    .as("%s : parenthesis index : %d", typeScriptText, i);
        }
        new IntegerAssert(p).isEqualTo(0).as(typeScriptText);
    }
}
