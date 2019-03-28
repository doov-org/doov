/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class TryTest {

    @Test
    void testMapSuccess() {

        Try<String> res = Try.success("yay!");

        Try<String> transformed = res.map(String::toUpperCase);

        Assertions.assertThat(transformed.isSuccess()).isTrue();
        Assertions.assertThat(transformed.value()).isEqualTo("YAY!");
    }

    @Test
    void testMapFailure() {

        Exception fail1 = new Exception("failure");

        Try<String> res = Try.failure(fail1);

        Try<String> transformed = res.map(String::toUpperCase);

        Assertions.assertThat(transformed.isFailure()).isTrue();
        Assertions.assertThat(transformed.error()).isEqualTo(fail1);
    }

    @Test
    void testCombineSuccesses() {

        Try<String> hello = Try.success("Hello,");
        Try<String> world = Try.success(" World");

        Try<String> res = Try.combine(String::concat, hello, world);

        Assertions.assertThat(res.isSuccess()).isTrue();
        Assertions.assertThat(res.value()).isEqualTo("Hello, World");
    }

    @Test
    void testCombineSuccessFailure() {

        Try<String> succ = Try.success("Hello,");
        Try<String> fail = Try.failure(new Exception("failure"));

        Try<String> res1 = Try.combine(String::concat, succ, fail);
        Try<String> res2 = Try.combine(String::concat, fail, succ);

        Assertions.assertThat(res1.isFailure()).isTrue();
        Assertions.assertThat(res2.isFailure()).isTrue();
        Assertions.assertThat(res1.error()).isEqualTo(res2.error());
    }

    @Test
    void testCombineFailures() {

        Exception throw1 = new Exception("failure");
        Exception throw2 = new Exception("another failure");

        Try<String> fail1 = Try.failure(throw1);
        Try<String> fail2 = Try.failure(throw2);

        Try<String> fails = Try.combine(String::concat, fail1, fail2);

        Assertions.assertThat(fails.isFailure()).isTrue();
        Assertions.assertThat(fails.error()).isEqualTo(throw1);
        Assertions.assertThat(fails.error()).isEqualTo(throw1);
    }

    @Test
    void testCatchable() {

        Try<Boolean> success = Try.callable(() -> true);
        Try<Boolean> failure = Try.callable(() -> {
            throw new Exception("FAIL");
        });

        Assertions.assertThat(success.isSuccess()).isTrue();
        Assertions.assertThat(failure.isFailure()).isTrue();

    }

    @Test
    void testEmpty() {
        Try<Void> empty1 = Try.empty();
        Try<Void> empty2 = Try.empty();
        Assertions.assertThat(empty1 == empty2).isTrue();
    }

    @Test
    void testValueOnError() {
        IllegalStateException causeError = new IllegalStateException("adadgd");
        Try<Object> illegalState = Try.failure(causeError);
        Assertions.assertThatThrownBy(() -> illegalState.value())
                .isInstanceOf(RuntimeException.class)
                .hasCause(causeError);
    }

    @Test
    void testErrorOnValue() {
        Try<String> some_value = Try.success("some value");
        Assertions.assertThat(some_value.error()).isNull();
    }

    @Test
    void testHandlers() {
        List<String> objects = new ArrayList<>();
        Try<String> map = Try.callable(() -> {
            System.out.println("1");
            objects.add("1");
            return "something";
        }).map(s -> {
            System.out.println("2");
            objects.add("2");
            return s;
        });

        objects.add(map.value());
        Assertions.assertThat(objects).containsExactly("1", "2", "something");
    }
}
