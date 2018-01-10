/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation.i18n;

import static io.doov.core.dsl.meta.DefaultOperator.always_true;
import static io.doov.core.dsl.meta.DefaultOperator.rule;
import static io.doov.core.dsl.meta.DefaultOperator.today;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;

public class OperatorResourceBundleTest {

    @Test
    public void always_true() {
        assertThat(BUNDLE.get(always_true, Locale.US)).isEqualTo("always true");
    }

    @Test
    public void always_true_fr_FR() {
        assertThat(BUNDLE.get(always_true, Locale.FRANCE)).isEqualTo("toujours vrai");
    }

    @Test
    public void rule() {
        assertThat(BUNDLE.get(rule, Locale.US)).isEqualTo("rule");
    }

    @Test
    public void rule_fr_FR() {
        assertThat(BUNDLE.get(rule, Locale.FRANCE)).isEqualTo("règle");
    }

    @Test
    public void today() {
        assertThat(BUNDLE.get(today, Locale.US)).isEqualTo("today");
    }

    @Test
    public void today_fr_FR() {
        assertThat(BUNDLE.get(today, Locale.FRANCE)).isEqualTo("date du jour");
    }
}
