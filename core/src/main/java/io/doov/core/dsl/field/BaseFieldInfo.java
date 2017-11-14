package io.doov.core.dsl.field;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.lang.StepCondition;

public interface BaseFieldInfo<T> extends DslField {

    // available

    default StepCondition available() {
        return getTypeCondition().available();
    }

    default StepCondition notAvailable() {
        return getTypeCondition().notAvailable();
    }

    // eq

    default StepCondition eq(T value) {
        return getTypeCondition().eq(value);
    }

    default StepCondition eq(DefaultFieldInfo<T> value) {
        return getTypeCondition().eq(value);
    }

    default StepCondition eq(Supplier<T> value) {
        return getTypeCondition().eq(value);
    }

    default StepCondition notEq(T value) {
        return getTypeCondition().notEq(value);
    }

    default StepCondition notEq(DefaultFieldInfo<T> value) {
        return getTypeCondition().notEq(value);
    }

    // null

    default StepCondition isNull() {
        return getTypeCondition().isNull();
    }

    default StepCondition isNotNull() {
        return getTypeCondition().isNotNull();
    }

    // match

    @SuppressWarnings("unchecked")
    default StepCondition anyMatch(T... values) {
        return getTypeCondition().anyMatch(values);
    }

    @SuppressWarnings("unchecked")
    default StepCondition anyMatch(Predicate<T> value) {
        return getTypeCondition().anyMatch(value);
    }

    @SuppressWarnings("unchecked")
    default StepCondition anyMatch(Predicate<T>... values) {
        return getTypeCondition().anyMatch(values);
    }

    default StepCondition anyMatch(Collection<T> values) {
        return getTypeCondition().anyMatch(values);
    }

    @SuppressWarnings("unchecked")
    default StepCondition allMatch(T... values) {
        return getTypeCondition().allMatch(values);
    }

    @SuppressWarnings("unchecked")
    default StepCondition allMatch(Predicate<T> value) {
        return getTypeCondition().allMatch(value);
    }

    @SuppressWarnings("unchecked")
    default StepCondition allMatch(Predicate<T>... values) {
        return getTypeCondition().allMatch(values);
    }

    default StepCondition allMatch(Collection<T> values) {
        return getTypeCondition().allMatch(values);
    }

    @SuppressWarnings("unchecked")
    default StepCondition noneMatch(T... values) {
        return getTypeCondition().noneMatch(values);
    }

    @SuppressWarnings("unchecked")
    default StepCondition noneMatch(Predicate<T> value) {
        return getTypeCondition().noneMatch(value);
    }

    @SuppressWarnings("unchecked")
    default StepCondition noneMatch(Predicate<T>... values) {
        return getTypeCondition().noneMatch(values);
    }

    default StepCondition noneMatch(Collection<T> values) {
        return getTypeCondition().noneMatch(values);
    }

    // implementation

    DefaultCondition<T> getTypeCondition();

}
