/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.*;
import java.util.stream.Stream;

/*
 * Name inspired by RxJava
 */

public interface Try<T> {

    class TryValue<V> implements Try<V> {

        private static final Try<?> EMPTY = new TryValue<>();

        private final V value;

        private TryValue() {
            this.value = null;
        }

        TryValue(V value) {
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public V value() {
            return value;
        }

    }

    class TryError<T> implements Try<T> {

        private final Exception error;

        TryError(Exception error) {
            this.error = error;
        }

        @Override
        public Exception error() {
            return error;
        }

    }

    static <S> Try<S> success(S value) {
        return (value == null) ? empty() : new TryValue<>(value);
    }

    static <S> Try<S> failure(Exception error) {
        return new TryError<>(error);
    }

    static <S> Try<S> empty() {
        @SuppressWarnings("unchecked")
        Try<S> empty = (Try<S>) TryValue.EMPTY;
        return empty;
    }

    static <S> Try<S> callable(Callable<S> supplier) {
        try {
            return success(supplier.call());
        } catch (Exception Exception) {
            return failure(Exception);
        }
    }

    static <S> Try<S> supplier(Supplier<S> supplier) {
        try {
            return success(supplier.get());
        } catch (Exception Exception) {
            return failure(Exception);
        }
    }

    static <S> Try<S> flatten(Try<Try<S>> nested) {
        if (nested.isSuccess()) {
            return nested.value();
        } else {
            return failure(nested.error());
        }
    }

    static <T, S, R> Try<R> combine(BiFunction<T, S, R> combinator, Try<T> lhs, Try<S> rhs) {
        if (lhs.isSuccess() && rhs.isSuccess()) {
            if (lhs.isNotNull() && rhs.isNotNull()) {
                return success(combinator.apply(lhs.value(), rhs.value()));
            } else {
                return empty();
            }
        } else if (lhs.isSuccess() && rhs.isFailure()) {
            return failure(rhs.error());
        } else if (lhs.isFailure() && rhs.isSuccess()) {
            return failure(lhs.error());
        } else { // lhs.isFailure() && rhs.isFailure()
            return failure(lhs.error());
        }

    }

    default T value() {
        throw new RuntimeException("No value present", error());
    }

    default Exception error() {
        return null;
    }

    default boolean isSuccess() {
        return error() == null;
    }

    default boolean isFailure() {
        return error() != null;
    }

    default boolean isNotNull() {
        return value() != null;
    }

    default boolean isNull() {
        return value() == null;
    }

    default <S> Try<S> map(Function<T, S> f) {
        Objects.requireNonNull(f);
        if (isSuccess()) {
            return supplier(() -> f.apply(value()));
        } else {
            return failure(error());
        }
    }

    default <S> Try<S> flatMap(Function<T, Try<S>> f) {
        Objects.requireNonNull(f);
        return flatten(map(f));
    }

    default Try<T> doOnSuccess(Consumer<T> consumer) {
        if (isSuccess()) {
            consumer.accept(value());
        }
        return this;
    }

    default Try<T> doOnError(Consumer<Exception> exceptionConsumer) {
        Objects.requireNonNull(exceptionConsumer);
        if (isFailure()) {
            exceptionConsumer.accept(error());
        }
        return this;
    }

    default Try<T> onErrorReturn(T value) {
        if (isSuccess()) {
            return this;
        } else {
            return Try.success(value);
        }
    }

    default Try<T> onErrorThrow() {
        if (isSuccess()) {
            return this;
        } else {
            if (error() instanceof RuntimeException) {
                throw (RuntimeException) error();
            } else {
                throw new RuntimeException(error());
            }
        }
    }

    default Stream<T> stream() {
        if (isSuccess()) {
            return Stream.of(value());
        } else {
            return Stream.empty();
        }
    }

    default Optional<T> optional() {
        if (isSuccess()) {
            return Optional.of(value());
        } else {
            return Optional.empty();
        }
    }

}
