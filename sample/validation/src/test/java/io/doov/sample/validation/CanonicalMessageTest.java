/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import static io.doov.core.dsl.DOOV.alwaysFalse;
import static io.doov.core.dsl.DOOV.alwaysTrue;
import static io.doov.core.dsl.DOOV.matchAll;
import static io.doov.core.dsl.DOOV.matchAny;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.doov.core.BaseFieldModel;
import io.doov.core.FieldModel;
import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.EmptyMetadata;
import io.doov.core.dsl.meta.FieldMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.NaryMetadata;
import io.doov.core.dsl.meta.UnaryMetadata;

public class CanonicalMessageTest {
    private final FieldModel model = new BaseFieldModel(emptyList());

    @Test
    public void combined_and_or() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse()).and(alwaysTrue().or(alwaysFalse())))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result.isTrue()).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().childs().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(1)).isInstanceOf(BinaryMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(msg.readable());
    }

    @Test
    public void combined_not_and_or() {
        final Result result = DOOV.when(alwaysTrue().and(alwaysFalse()).or(alwaysTrue().and(alwaysFalse())).not())
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result.isTrue()).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(UnaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(0).childs().get(0))
                        .isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(0).childs().get(1))
                        .isInstanceOf(BinaryMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(msg.readable());
    }

    @Test
    public void not_false() {
        final Result result = DOOV.when(alwaysFalse().not())
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result.isTrue()).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(UnaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(0)).isInstanceOf(FieldMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        System.out.println(msg.readable());
    }

    @Test
    public void matchAll_level_1() {
        final Result result = DOOV.when(matchAll(alwaysTrue(), alwaysTrue(), alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result.isTrue()).isFalse();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(3);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(EmptyMetadata.class);

        System.out.println(msg.readable());
    }

    @Test
    public void matchAny_level_1() {
        final Result result = DOOV.when(matchAny(alwaysTrue(), alwaysTrue(), alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result.isTrue()).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(3);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(NaryMetadata.class);
        assertThat(msg.childs()).hasSize(2);

        System.out.println(msg.readable());
    }

    @Test
    public void and_level_1() {
        final Result result = DOOV.when(alwaysTrue().and(alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result.isTrue()).isFalse();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(EmptyMetadata.class);

        System.out.println(msg.readable());
    }

    @Test
    public void or_level_1() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result.isTrue()).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(FieldMetadata.class);
        assertThat(msg.childs()).isEmpty();

        System.out.println(msg.readable());
    }

    @Test
    public void or_level_2() {
        final Result result = DOOV.when(alwaysFalse().or(alwaysFalse().or(alwaysTrue())))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result.isTrue()).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().childs().get(1)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(1).childs()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(FieldMetadata.class);
        assertThat(msg.childs()).isEmpty();

        System.out.println(msg.readable());
    }

    @Test
    public void or_level_1_and_2() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse().or(alwaysTrue())))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result.isTrue()).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().childs().get(1)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(1).childs()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.childs()).hasSize(2);
        assertThat(msg.childs().get(0)).isInstanceOf(FieldMetadata.class);
        assertThat(msg.childs().get(1)).isInstanceOf(FieldMetadata.class);

        System.out.println(msg.readable());
    }

}
