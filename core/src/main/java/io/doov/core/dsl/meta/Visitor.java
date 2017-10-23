package io.doov.core.dsl.meta;

public interface Visitor<T> {

    void accept(T t);

}
