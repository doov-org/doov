/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

/*
 * Name inspired by RxJava
 */

public class Try<T> {

    private final T value;
    private final List<Throwable> reasons;

    private final boolean hasValue;

    private Try(T value, List<Throwable> reasons, boolean hasValue) {
        this.value = value;
        this.reasons = reasons;
        this.hasValue = hasValue;
    }

    public static <S> Try<S> success(S value) {
        return new Try<>(value, null, true);
    }

    public static <S> Try<S> failure(Throwable... fails) {
        return failure(Arrays.asList(fails));
    }

    public static <S> Try<S> failure(List<Throwable> fails) {
        return new Try<>(null, fails, false);
    }

    public interface SafeSupplier<T,E extends Throwable> {
        T get() throws E;
    }

    public static <S> Try<S> catchable(SafeSupplier<S,?> supplier) {
        try {
            return success(supplier.get());
        } catch (Throwable throwable) {
            return failure(throwable);
        }
    }

    public static <S> Try<S> supplied(Supplier<S> supplier) {
        try {
            return success(supplier.get());
        } catch (Throwable throwable) {
            return failure(throwable);
        }
    }

    public boolean isSuccess() {
        return hasValue;
    }

    public boolean isFailure() {
        return !hasValue;
    }

    public boolean isNotNull() {
        return value != null;
    }
    public boolean isNull() {
        return value == null;
    }

    public T value() {
        return value;
    }

    public List<Throwable> reasons() {
        return reasons;
    }

    public <S> Try<S> map(Function<T,S> f) {
        if(hasValue && isNotNull()) {
            return success(f.apply(value));
        } else if (hasValue) {
            return success(null);
        } else {
            return failure(reasons);
        }
    }

    public static <S> Try<S> flatten(Try<Try<S>> nested) {
        if(nested.isSuccess()) {
            return nested.value;
        } else {
            return failure(new Throwable("ERROR in FLATTEN")); // Never reached
        }
    }

    public <S> Try<S> flatMap(Function<T, Try<S>> f) {
        return flatten(map(f));
    }

    public Try<T> recover(T value) {
        if(hasValue && isNotNull()) {
            return this;
        } else {
            return Try.success(value);
        }
    }

    public static <T,S,R> Try<R> combine(BiFunction<T,S,R> combinator, Try<T> lhs, Try<S> rhs) {

        if(lhs.isSuccess() && rhs.isSuccess()) {
            if(lhs.isNotNull() && rhs.isNotNull()) {
                return success(combinator.apply(lhs.value,rhs.value));
            } else {
                return success(null);
            }
        }

        if(lhs.isSuccess() && rhs.isFailure()) {
            return failure(rhs.reasons);
        }

        if(lhs.isFailure() && rhs.isSuccess()) {
            return failure(lhs.reasons);
        }

        if(lhs.isFailure() && rhs.isFailure()) {
            List<Throwable> res = new ArrayList<>();
            res.addAll(lhs.reasons);
            res.addAll(rhs.reasons);
            return failure(res);
        }

        throw new RuntimeException(); // Never triggered
    }

    public Stream<T> stream() {
        if(isNotNull()) {
            return Stream.of(value);
        } else {
            return Stream.empty();
        }
    }

}
