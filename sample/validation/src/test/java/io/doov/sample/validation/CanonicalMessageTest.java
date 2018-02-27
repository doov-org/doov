/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.matchAny;
import static io.doov.core.dsl.DOOV.sum;
import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.count;
import static io.doov.core.dsl.meta.DefaultOperator.sum;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE_MATCH_ANY;
import static io.doov.sample.field.dsl.DslSampleModel.accountCountry;
import static io.doov.sample.field.dsl.DslSampleModel.configurationMaxEmailSize;
import static io.doov.sample.field.dsl.DslSampleModel.configurationMinAge;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.core.BaseFieldModel;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.EmptyMetadata;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.NaryMetadata;
import io.doov.core.dsl.meta.UnaryMetadata;
import io.doov.sample.field.SampleFieldId;
import io.doov.sample.model.Country;

public class CanonicalMessageTest {
    private final FieldModel model = new BaseFieldModel(emptyList());

    @Test
    void matchAny_values() {
        model.set(SampleFieldId.COUNTRY, Country.FR);
        final Result result = DOOV.when(accountCountry().anyMatch(Country.FR, Country.CAN)).validate()
                        .withShortCircuit(false).executeOn(model);
        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();
        assertThat(result.getContext().getEvalValue(SampleFieldId.COUNTRY)).isEqualTo(Country.FR);
        assertThat(result.getContext().getRootMetadata()).isInstanceOf(LeafMetadata.class);
        assertThat(result.getContext().getRootMetadata().type()).isEqualTo(FIELD_PREDICATE_MATCH_ANY);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(LeafMetadata.class);
        assertThat(msg.type()).isEqualTo(FIELD_PREDICATE);
    }

    @Test
    public void combined_and_or() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse()).and(alwaysTrue().or(alwaysFalse())))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(1)).isInstanceOf(BinaryMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.children()).hasSize(2);
        assertThat(msg.children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(msg.children().get(1)).isInstanceOf(LeafMetadata.class);
    }

    @Test
    public void combined_or_matchAny() {
        final Result result = DOOV.when(matchAny(alwaysTrue().or(alwaysFalse()), alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(LeafMetadata.class);
    }

    @Test
    public void combined_not_and_or() {
        final Result result = DOOV.when(alwaysTrue().and(alwaysFalse()).or(alwaysTrue().and(alwaysFalse())).not())
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(UnaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0).children().get(0))
                        .isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0).children().get(1))
                        .isInstanceOf(BinaryMetadata.class);

        // message is not simplified because the expression use a not at the top level
        // look at 'boolean satisfiability problem' for more details
        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(UnaryMetadata.class);
        assertThat(msg.children().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.children().get(0).children().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.children().get(0).children().get(1)).isInstanceOf(BinaryMetadata.class);
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void not_false() {
        final Result result = DOOV.when(alwaysFalse().not())
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(UnaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0)).isInstanceOf(LeafMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(UnaryMetadata.class);
    }

    @Test
    public void simple_matchAll() {
        final Result result = DOOV.when(matchAll(alwaysTrue(), alwaysTrue(), alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isFalse();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(3);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    public void simple_matchAny() {
        final Result result = DOOV.when(matchAny(alwaysTrue(), alwaysTrue(), alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(3);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(NaryMetadata.class);
        assertThat(msg.children()).hasSize(2);
    }

    @Test
    public void simple_and() {
        final Result result = DOOV.when(alwaysTrue().and(alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isFalse();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(LeafMetadata.class);
        assertThat(msg.children()).isEmpty();
    }

    @Test
    public void simple_or() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(LeafMetadata.class);
        assertThat(msg.children()).isEmpty();
    }

    @Test
    public void combined_or_1() {
        final Result result = DOOV.when(alwaysFalse().or(alwaysFalse().or(alwaysTrue())))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().get(1)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(1).children()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(LeafMetadata.class);
        assertThat(msg.children()).isEmpty();
    }

    @Test
    public void combined_or_2() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse().or(alwaysTrue())))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().get(1)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(1).children()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.children()).hasSize(2);
        assertThat(msg.children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(msg.children().get(1)).isInstanceOf(LeafMetadata.class);
    }

    @Test
    public void sum_numeric_fields() {
        model.set(configurationMaxEmailSize().id(), 3);
        model.set(configurationMinAge().id(), 0);
        final Result result = DOOV.when(sum(configurationMaxEmailSize(), configurationMinAge()).greaterThan(2))
                        .validate().withShortCircuit(false).executeOn(model);
        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(((NaryMetadata) result.getContext().getRootMetadata().children().get(0)).getOperator())
                        .isEqualTo(sum);
        assertThat(result.getContext().getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0).children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().get(0).children().get(0))
                        .isInstanceOf(LeafMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0).children().get(1))
                        .isInstanceOf(LeafMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.children()).hasSize(2);
        assertThat(msg.children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(msg.children().get(0).children()).hasSize(1);
        assertThat(msg.children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(((NaryMetadata) msg.children().get(0)).getOperator()).isEqualTo(sum);
        assertThat(msg.children().get(0).children().get(0)).isInstanceOf(LeafMetadata.class);
    }

    @Test
    public void sum_numeric_condition() {
        model.set(configurationMaxEmailSize().id(), 1);
        model.set(configurationMinAge().id(), 0);
        final Result result = DOOV.when(sum(configurationMaxEmailSize().times(3), configurationMinAge().times(20))
                        .greaterThan(2)).validate().withShortCircuit(false).executeOn(model);
        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(((NaryMetadata) result.getContext().getRootMetadata().children().get(0)).getOperator())
                        .isEqualTo(sum);
        assertThat(result.getContext().getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0).children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().get(0).children().get(0))
                        .isInstanceOf(LeafMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0).children().get(1))
                        .isInstanceOf(LeafMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.children()).hasSize(2);
        assertThat(msg.children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(msg.children().get(0).children()).hasSize(1);
        assertThat(msg.children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(((NaryMetadata) msg.children().get(0)).getOperator()).isEqualTo(sum);
        assertThat(msg.children().get(0).children().get(0)).isInstanceOf(LeafMetadata.class);
    }

    @Test
    public void count() {
        final Result result = DOOV.when(DOOV.count(alwaysTrue(), alwaysTrue(), alwaysFalse())
                        .greaterOrEquals(2)).validate().withShortCircuit(false).executeOn(model);
        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().get(0)).isInstanceOf(NaryMetadata.class);
        assertThat(((NaryMetadata) result.getContext().getRootMetadata().children().get(0)).getOperator())
                        .isEqualTo(count);
        assertThat(result.getContext().getRootMetadata().children().get(1)).isInstanceOf(LeafMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0).children()).hasSize(3);
        assertThat(result.getContext().getRootMetadata().children().get(0).children().get(0))
                        .isInstanceOf(LeafMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0).children().get(1))
                        .isInstanceOf(LeafMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().get(0).children().get(2))
                        .isInstanceOf(LeafMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.children()).hasSize(2);
        assertThat(((BinaryMetadata) msg).getOperator()).isEqualTo(and);      
        assertThat(msg.children().get(0)).isInstanceOf(LeafMetadata.class);
        assertThat(msg.children().get(1)).isInstanceOf(LeafMetadata.class);
    }
}
