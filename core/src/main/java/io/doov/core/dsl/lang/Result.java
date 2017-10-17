package io.doov.core.dsl.lang;

public interface Result {

    boolean isValid();

    boolean isInvalid();

    String getMessage();

}
