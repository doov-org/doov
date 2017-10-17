package io.doov.core.dsl.lang;

import java.util.List;

import io.doov.core.dsl.meta.Metadata;

public interface Result {

    boolean isValid();

    boolean isInvalid();

    String getMessage();

    List<Metadata> getFailedNodes();

}
