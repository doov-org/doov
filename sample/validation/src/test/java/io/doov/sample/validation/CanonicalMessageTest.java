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
import static io.doov.core.dsl.lang.ReduceType.SUCCESS;
import static io.doov.core.dsl.meta.DefaultOperator.count;
import static io.doov.core.dsl.meta.DefaultOperator.sum;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE_MATCH_ANY;
import static io.doov.core.dsl.lang.ReduceType.FAILURE;
import static io.doov.sample.field.dsl.DslSampleModel.accountCountry;
import static io.doov.sample.field.dsl.DslSampleModel.configurationMaxEmailSize;
import static io.doov.sample.field.dsl.DslSampleModel.configurationMinAge;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.core.BaseFieldModel;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.ReduceType;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.EmptyMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;
import io.doov.core.dsl.meta.predicate.NaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.UnaryPredicateMetadata;
import io.doov.sample.field.SampleFieldId;
import io.doov.sample.model.Country;

public class CanonicalMessageTest {
    private final FieldModel model = new BaseFieldModel(emptyList());

    @Test
    public void matchAny_sum() {
        model.set(SampleFieldId.CONFIGURATION_EMAIL_MAX_SIZE, 1);
        final Result result = DOOV
                .when(matchAny(sum(configurationMaxEmailSize).greaterThan(100)))
                .validate().withShortCircuit(false).executeOn(model);
        assertThat(result).isFalse();
        System.out.println(result.getFailureCause());
    }

    @Test
    public void matchAny_sum_reduce() {
        model.set(SampleFieldId.CONFIGURATION_EMAIL_MAX_SIZE, 200);
        final Result result = DOOV
                .when(matchAny(sum(configurationMaxEmailSize).greaterThan(100)))
                .validate().withShortCircuit(false).executeOn(model);
        assertThat(result).isTrue();
        System.out.println(result.reduce(ReduceType.SUCCESS));
    }

    @Test
    public void matchAny_values() {
        model.set(SampleFieldId.COUNTRY, Country.FR);
        final Result result = DOOV.when(accountCountry.anyMatch(Country.FR, Country.CAN)).validate()
                .withShortCircuit(false).executeOn(model);
        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();
        assertThat(result.getContext().getEvalValue(SampleFieldId.COUNTRY)).isEqualTo(Country.FR);
        assertThat(result.getContext().getRootMetadata()).isInstanceOf(LeafPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().type()).isEqualTo(FIELD_PREDICATE_MATCH_ANY);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), FAILURE);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(LeafPredicateMetadata.class);
        assertThat(msg.type()).isEqualTo(FIELD_PREDICATE);
    }

    @Test
    public void combined_and_or() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse()).and(alwaysTrue().or(alwaysFalse())))
                .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children()).element(0)
                .isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).element(1)
                .isInstanceOf(BinaryPredicateMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), FAILURE);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    public void combined_or_matchAny() {
        final Result result = DOOV.when(matchAny(alwaysTrue().or(alwaysFalse()), alwaysFalse()))
                .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children()).element(0)
                .isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(1))
                .isInstanceOf(LeafPredicateMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), SUCCESS);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(LeafPredicateMetadata.class);
    }

    @Test
    public void combined_not_and_or() {
        final Result result = DOOV.when(alwaysTrue().and(alwaysFalse()).or(alwaysTrue().and(alwaysFalse())).not())
                .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(UnaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).element(0)
                .isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()
                .collect(toList()).get(0)).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()
                .collect(toList()).get(1)).isInstanceOf(BinaryPredicateMetadata.class);

        // message is not simplified because the expression use a not at the top level
        // look at 'boolean satisfiability problem' for more details
        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), FAILURE);
        assertThat(msg).isInstanceOf(UnaryPredicateMetadata.class);
        assertThat(msg.children()).element(0).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(msg.children().collect(toList()).get(0).children()).element(0)
                .isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(msg.children().collect(toList()).get(0).children()).element(1)
                .isInstanceOf(BinaryPredicateMetadata.class);
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void not_false() {
        final Result result = DOOV.when(alwaysFalse().not())
                .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(UnaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).element(0)
                .isInstanceOf(LeafPredicateMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), FAILURE);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(UnaryPredicateMetadata.class);
    }

    @Test
    public void simple_matchAll() {
        final Result result = DOOV.when(matchAll(alwaysTrue(), alwaysTrue(), alwaysFalse()))
                .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isFalse();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(3);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), FAILURE);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(LeafPredicateMetadata.class);
    }

    @Test
    public void simple_matchAny() {
        final Result result = DOOV.when(matchAny(alwaysTrue(), alwaysTrue(), alwaysFalse()))
                .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(3);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), SUCCESS);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(NaryPredicateMetadata.class);
    }

    @Test
    public void simple_and() {
        final Result result = DOOV.when(alwaysTrue().and(alwaysFalse()))
                .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isFalse();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), FAILURE);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(LeafPredicateMetadata.class);
        assertThat(msg.children()).isEmpty();
    }

    @Test
    public void simple_or() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse()))
                .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), FAILURE);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    public void combined_or_1() {
        final Result result = DOOV.when(alwaysFalse().or(alwaysFalse().or(alwaysTrue())))
                .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children()).element(1)
                .isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(1).children()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), FAILURE);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    public void combined_or_2() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse().or(alwaysTrue())))
                .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children()).element(1)
                .isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(1).children()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), FAILURE);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(EmptyMetadata.class);
    }

    @Test
    public void sum_numeric_fields() {
        model.set(configurationMaxEmailSize.id(), 3);
        model.set(configurationMinAge.id(), 0);
        final Result result = DOOV.when(sum(configurationMaxEmailSize, configurationMinAge).greaterThan(2))
                .validate().withShortCircuit(false).executeOn(model);
        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children()).element(0)
                .isInstanceOf(NaryPredicateMetadata.class);
        assertThat(((NaryPredicateMetadata) result.getContext().getRootMetadata().children().collect(toList()).get(0))
                .getOperator()).isEqualTo(sum);
        assertThat(result.getContext().getRootMetadata().children()).element(1)
                .isInstanceOf(LeafPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()).element(0)
                .isInstanceOf(LeafPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()).element(1)
                .isInstanceOf(LeafPredicateMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), SUCCESS);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(msg.children()).hasSize(2);
        assertThat(msg.children()).element(0).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(msg.children().collect(toList()).get(0).children()).hasSize(1);
        assertThat(msg.children()).element(0).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(((NaryPredicateMetadata) msg.children().collect(toList()).get(0)).getOperator()).isEqualTo(sum);
        assertThat(msg.children().collect(toList()).get(0).children()).element(0)
                .isInstanceOf(LeafPredicateMetadata.class);
    }

    @Test
    public void sum_numeric_condition() {
        model.set(configurationMaxEmailSize.id(), 1);
        model.set(configurationMinAge.id(), 0);
        final Result result = DOOV.when(sum(configurationMaxEmailSize.times(3), configurationMinAge.times(20))
                .greaterThan(2)).validate().withShortCircuit(false).executeOn(model);
        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children()).element(0)
                .isInstanceOf(NaryPredicateMetadata.class);
        assertThat(((NaryPredicateMetadata) result.getContext().getRootMetadata().children().collect(toList()).get(0))
                .getOperator()).isEqualTo(sum);
        assertThat(result.getContext().getRootMetadata().children()).element(1)
                .isInstanceOf(LeafPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()).element(0)
                .isInstanceOf(LeafPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()).element(1)
                .isInstanceOf(LeafPredicateMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), SUCCESS);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(msg.children()).hasSize(2);
        assertThat(msg.children()).element(0).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(msg.children().collect(toList()).get(0).children()).hasSize(1);
        assertThat(msg.children()).element(0).isInstanceOf(NaryPredicateMetadata.class);
        assertThat(((NaryPredicateMetadata) msg.children().collect(toList()).get(0)).getOperator()).isEqualTo(sum);
        assertThat(msg.children().collect(toList()).get(0).children()).element(0)
                .isInstanceOf(LeafPredicateMetadata.class);
    }

    @Test
    public void count() {
        final Result result = DOOV.when(DOOV.count(alwaysTrue(), alwaysTrue(), alwaysFalse())
                .greaterOrEquals(2)).validate().withShortCircuit(false).executeOn(model);
        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().children()).element(0)
                .isInstanceOf(NaryPredicateMetadata.class);
        assertThat(((NaryPredicateMetadata) result.getContext().getRootMetadata().children().collect(toList()).get(0))
                .getOperator()).isEqualTo(count);
        assertThat(result.getContext().getRootMetadata().children()).element(1)
                .isInstanceOf(LeafPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList())
                .get(0).children()).hasSize(3);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()).element(0)
                .isInstanceOf(LeafPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()).element(1)
                .isInstanceOf(LeafPredicateMetadata.class);
        assertThat(result.getContext().getRootMetadata().children().collect(toList()).get(0).children()).element(2)
                .isInstanceOf(LeafPredicateMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().reduce(result.getContext(), SUCCESS);
        System.out.println(">> " + msg.readable());

        assertThat(msg).isInstanceOf(BinaryPredicateMetadata.class);
    }
}
