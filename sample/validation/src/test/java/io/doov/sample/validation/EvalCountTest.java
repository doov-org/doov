/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.count;
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.sample.field.dsl.DslSampleModel.accountCountry;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.core.BaseFieldModel;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.PredicateMetadata;
import io.doov.sample.field.SampleFieldId;
import io.doov.sample.model.Country;

public class EvalCountTest {
    private final FieldModel model = new BaseFieldModel(emptyList());

    @Test
    void matchAny_values() {
        model.set(SampleFieldId.COUNTRY, Country.FR);
        ValidationRule rule = DOOV.when(accountCountry().anyMatch(Country.FR, Country.CAN)).validate()
                        .withShortCircuit(false);

        rule.executeOn(model);
        rule.executeOn(model);
        model.set(SampleFieldId.COUNTRY, Country.US);
        Result result = rule.executeOn(model);

        PredicateMetadata rm = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rm.trueEvalCount()).isEqualTo(2);
        assertThat(rm.falseEvalCount()).isEqualTo(1);
    }

    @Test
    public void combined_and_or() {
        ValidationRule rule = DOOV.when(alwaysTrue().or(alwaysFalse()).and(alwaysTrue().or(alwaysFalse())))
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);

        PredicateMetadata rm = (PredicateMetadata) result.getContext().getRootMetadata();
        assertThat(rm.trueEvalCount()).isEqualTo(1);
        assertThat(rm.falseEvalCount()).isEqualTo(0);

        result = rule.executeOn(model);
        rm = (PredicateMetadata) result.getContext().getRootMetadata();
        assertThat(rm.trueEvalCount()).isEqualTo(2);
        assertThat(rm.falseEvalCount()).isEqualTo(0);

    }

    @Test
    public void combined_or_matchAny() {
        ValidationRule rule = DOOV.when(matchAny(alwaysTrue().or(alwaysFalse()), alwaysFalse()))
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);
        PredicateMetadata rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();
        assertThat(rootMetadata.trueEvalCount()).isEqualTo(1);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);

        result = rule.executeOn(model);
        rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();
        assertThat(rootMetadata.trueEvalCount()).isEqualTo(2);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);


    }

    @Test
    public void combined_not_and_or() {
        ValidationRule rule = DOOV.when(alwaysTrue().and(alwaysFalse()).or(alwaysTrue().and(alwaysFalse())).not())
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);
        PredicateMetadata rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(1);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);

        result = rule.executeOn(model);
        rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(2);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);
    }

    @Test
    public void not_false() {
        ValidationRule rule =  DOOV.when(alwaysFalse().not())
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);
        PredicateMetadata rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(1);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);

        result = rule.executeOn(model);
        rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(2);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);
    }

    @Test
    public void simple_matchAll() {
        ValidationRule rule =  DOOV.when(matchAll(alwaysTrue(), alwaysTrue(), alwaysFalse()))
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);
        PredicateMetadata rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(0);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(1);

        result = rule.executeOn(model);
        rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(0);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(2);
    }

    @Test
    public void simple_matchAny() {
        ValidationRule rule = DOOV.when(matchAny(alwaysTrue(), alwaysTrue(), alwaysFalse()))
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);
        PredicateMetadata rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(1);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);

        result = rule.executeOn(model);
        rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(2);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);
    }

    @Test
    public void simple_and() {
        ValidationRule rule = DOOV.when(alwaysTrue().and(alwaysFalse()))
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);
        PredicateMetadata rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(0);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(1);

        result = rule.executeOn(model);
        rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(0);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(2);
    }

    @Test
    public void simple_or() {
        ValidationRule rule = DOOV.when(alwaysTrue().or(alwaysFalse()))
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);
        PredicateMetadata rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(1);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);

        result = rule.executeOn(model);
        rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(2);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);
    }

    @Test
    public void combined_or_1() {
        ValidationRule rule = DOOV.when(alwaysFalse().or(alwaysFalse().or(alwaysTrue())))
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);
        PredicateMetadata rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(1);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);

        result = rule.executeOn(model);
        rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(2);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);
    }

    @Test
    public void combined_or_2() {
        ValidationRule rule = DOOV.when(alwaysTrue().or(alwaysFalse().or(alwaysTrue())))
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);
        PredicateMetadata rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(1);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);

        result = rule.executeOn(model);
        rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(2);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);
    }

    @Test
    public void simple_count() {
        ValidationRule rule = DOOV.when(count(alwaysFalse(), alwaysTrue(), alwaysFalse()).greaterOrEquals(1))
                        .validate().withShortCircuit(false);
        Result result = rule.executeOn(model);
        PredicateMetadata rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(1);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);

        result = rule.executeOn(model);
        rootMetadata = (PredicateMetadata) result.getContext().getRootMetadata();

        assertThat(rootMetadata.trueEvalCount()).isEqualTo(2);
        assertThat(rootMetadata.falseEvalCount()).isEqualTo(0);
    }

}
