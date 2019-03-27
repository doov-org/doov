/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core;

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

        Throwable fail1 = new Throwable("failure");
        Throwable fail2 = new Throwable("another failure");

        Try<String> res = Try.failure(fail1,fail2);

        Try<String> transformed = res.map(String::toUpperCase);

        Assertions.assertThat(transformed.isFailure()).isTrue();
        Assertions.assertThat(transformed.reasons()).contains(fail1);
        Assertions.assertThat(transformed.reasons()).contains(fail2);
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
        Try<String> fail = Try.failure(new Throwable("failure"));

        Try<String> res1 = Try.combine(String::concat, succ, fail);
        Try<String> res2 = Try.combine(String::concat, fail, succ);

        Assertions.assertThat(res1.isFailure()).isTrue();
        Assertions.assertThat(res2.isFailure()).isTrue();
        Assertions.assertThat(res1.reasons()).isEqualTo(res2.reasons());
    }

    @Test
    void testCombineFailures() {

        Throwable throw1 = new Throwable("failure");
        Throwable throw2 = new Throwable("another failure");

        Try<String> fail1 = Try.failure(throw1);
        Try<String> fail2 = Try.failure(throw2);

        Try<String> fails = Try.combine(String::concat, fail1, fail2);

        Assertions.assertThat(fails.isFailure()).isTrue();
        Assertions.assertThat(fails.reasons()).contains(throw1);
        Assertions.assertThat(fails.reasons()).contains(throw2);
    }

    @Test
    void testCatchable() {

        Try<Boolean> success = Try.catchable(() -> true);
        Try<Boolean> failure = Try.catchable(() -> {throw new Throwable("FAIL");});

        Assertions.assertThat(success.isSuccess()).isTrue();
        Assertions.assertThat(failure.isFailure()).isTrue();

    }



}
