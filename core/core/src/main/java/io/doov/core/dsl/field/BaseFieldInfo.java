package io.doov.core.dsl.field;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.Collection;
import java.util.function.*;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.IntegerCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface BaseFieldInfo<T> extends DslField {

    // null

    default StepCondition isNull() {
        return getDefaultCondition().isNull();
    }

    default StepCondition isNotNull() {
        return getDefaultCondition().isNotNull();
    }

    // eq

    default StepCondition eq(T value) {
        return getDefaultCondition().eq(value);
    }

    default StepCondition eq(BaseFieldInfo<T> value) {
        return getDefaultCondition().eq(value);
    }

    default StepCondition eq(Supplier<T> value) {
        return getDefaultCondition().eq(value);
    }

    default StepCondition notEq(T value) {
        return getDefaultCondition().notEq(value);
    }

    default StepCondition notEq(BaseFieldInfo<T> value) {
        return getDefaultCondition().notEq(value);
    }

    // any match

    @SuppressWarnings("unchecked")
    default StepCondition anyMatch(Predicate<T> value) {
        return getDefaultCondition().anyMatch(singletonList(value));
    }

    @SuppressWarnings("unchecked")
    default StepCondition anyMatch(Predicate<T>... values) {
        return getDefaultCondition().anyMatch(asList(values));
    }

    @SuppressWarnings("unchecked")
    default StepCondition anyMatch(T... values) {
        return getDefaultCondition().anyMatch(asList(values));
    }

    default StepCondition anyMatch(Collection<T> values) {
        return getDefaultCondition().anyMatch(values);
    }

    // all match

    @SuppressWarnings("unchecked")
    default StepCondition allMatch(Predicate<T> value) {
        return getDefaultCondition().allMatch(singletonList(value));
    }

    @SuppressWarnings("unchecked")
    default StepCondition allMatch(Predicate<T>... values) {
        return getDefaultCondition().allMatch(asList(values));
    }

    @SuppressWarnings("unchecked")
    default StepCondition allMatch(T... values) {
        return getDefaultCondition().allMatch(asList(values));
    }

    default StepCondition allMatch(Collection<T> values) {
        return getDefaultCondition().allMatch(values);
    }

    // none match

    @SuppressWarnings("unchecked")
    default StepCondition noneMatch(Predicate<T> value) {
        return getDefaultCondition().noneMatch(singletonList(value));
    }

    @SuppressWarnings("unchecked")
    default StepCondition noneMatch(Predicate<T>... values) {
        return getDefaultCondition().noneMatch(asList(values));
    }

    @SuppressWarnings("unchecked")
    default StepCondition noneMatch(T... values) {
        return getDefaultCondition().noneMatch(asList(values));
    }

    default StepCondition noneMatch(Collection<T> values) {
        return getDefaultCondition().noneMatch(values);
    }

    // map

    default IntegerCondition mapToInt(Function<T, Integer> mapper) {
        return getDefaultCondition().mapToInt(mapper);
    }

    // implementation

    DefaultCondition<T> getDefaultCondition();

}
