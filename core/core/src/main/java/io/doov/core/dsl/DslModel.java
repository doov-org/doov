package io.doov.core.dsl;

public interface DslModel {
    <T> T get(DslId id);
}
