/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.ast;

import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.sample.validation.EmployeeMapping.ALL_MAPPINGS;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.meta.Metadata;
import io.doov.ts.ast.AstTSRenderer;
import io.doov.ts.ast.writer.DefaultTypeScriptWriter;
import io.doov.ts.ast.writer.TypeScriptWriter;

class TSEmployeeMappingTest {

    private static Stream<MappingRule> rules() {
        return Stream.concat(ALL_MAPPINGS.stream(), Stream.of(ALL_MAPPINGS));
    }

    @ParameterizedTest
    @MethodSource("rules")
    void name(MappingRule rule) {
        String ruleTs = toTS(rule.metadata());
        assertParenthesis(ruleTs);
        System.out.println(ruleTs);
        System.out.println();
    }

    private String toTS(Metadata metadata) {
        final ByteArrayOutputStream ops = new ByteArrayOutputStream();
        TypeScriptWriter writer = new DefaultTypeScriptWriter(Locale.US, ops, BUNDLE);
        new AstTSRenderer(writer).toTS(metadata);
        return new String(ops.toByteArray(), UTF_8);
    }

    void assertParenthesis(String rule) {
        int p = 0;
        char[] chars = rule.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(') {
                p++;
            }
            if (chars[i] == ')') {
                p--;
            }
            assertThat(p).isNotNegative()
                    .as("%s : parenthesis index : %d", rule, i);
        }
        assertThat(p).isEqualTo(0)
                .as(rule);
    }
}
