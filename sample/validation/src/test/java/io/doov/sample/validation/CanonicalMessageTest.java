/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import static io.doov.assertions.Assertions.assertThat;
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
import io.doov.core.dsl.meta.*;

public class CanonicalMessageTest {
    private final FieldModel model = new BaseFieldModel(emptyList());

    @Test
    public void combined_and_or() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse()).and(alwaysTrue().or(alwaysFalse())))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().childs().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(1)).isInstanceOf(BinaryMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.childs()).hasSize(2);
        assertThat(msg.childs().get(0)).isInstanceOf(FieldMetadata.class);
        assertThat(msg.childs().get(1)).isInstanceOf(FieldMetadata.class);
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void combined_or_matchAny() {
        final Result result = DOOV.when(matchAny(alwaysTrue().or(alwaysFalse()), alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().childs().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(1)).isInstanceOf(FieldMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(FieldMetadata.class);
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void combined_not_and_or() {
        final Result result = DOOV.when(alwaysTrue().and(alwaysFalse()).or(alwaysTrue().and(alwaysFalse())).not())
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(UnaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(0).childs().get(0))
                        .isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(0).childs().get(1))
                        .isInstanceOf(BinaryMetadata.class);

        // message is not simplified because the expression use a not at the top level
        // look at 'boolean satisfiability problem' for more details
        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(UnaryMetadata.class);
        assertThat(msg.childs().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.childs().get(0).childs().get(0)).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.childs().get(0).childs().get(1)).isInstanceOf(BinaryMetadata.class);
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void not_false() {
        final Result result = DOOV.when(alwaysFalse().not())
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(UnaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(0)).isInstanceOf(FieldMetadata.class);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(UnaryMetadata.class);
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void simple_matchAll() {
        final Result result = DOOV.when(matchAll(alwaysTrue(), alwaysTrue(), alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isFalse();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(3);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(EmptyMetadata.class);
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void simple_matchAny() {
        final Result result = DOOV.when(matchAny(alwaysTrue(), alwaysTrue(), alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(NaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(3);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(NaryMetadata.class);
        assertThat(msg.childs()).hasSize(2);
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void simple_and() {
        final Result result = DOOV.when(alwaysTrue().and(alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isFalse();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(EmptyMetadata.class);
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void simple_or() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse()))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(FieldMetadata.class);
        assertThat(msg.childs()).isEmpty();
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void combined_or_1() {
        final Result result = DOOV.when(alwaysFalse().or(alwaysFalse().or(alwaysTrue())))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().childs().get(1)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(1).childs()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(FieldMetadata.class);
        assertThat(msg.childs()).isEmpty();
        System.out.println(">> " + msg.readable());
    }

    @Test
    public void combined_or_2() {
        final Result result = DOOV.when(alwaysTrue().or(alwaysFalse().or(alwaysTrue())))
                        .validate().withShortCircuit(false).executeOn(model);

        System.out.println(result.getContext().getRootMetadata().readable());
        assertThat(result).isTrue();

        assertThat(result.getContext().getRootMetadata()).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs()).hasSize(2);
        assertThat(result.getContext().getRootMetadata().childs().get(1)).isInstanceOf(BinaryMetadata.class);
        assertThat(result.getContext().getRootMetadata().childs().get(1).childs()).hasSize(2);

        final Metadata msg = result.getContext().getRootMetadata().message(result.getContext());
        assertThat(msg).isInstanceOf(BinaryMetadata.class);
        assertThat(msg.childs()).hasSize(2);
        assertThat(msg.childs().get(0)).isInstanceOf(FieldMetadata.class);
        assertThat(msg.childs().get(1)).isInstanceOf(FieldMetadata.class);
        System.out.println(">> " + msg.readable());
    }

}
