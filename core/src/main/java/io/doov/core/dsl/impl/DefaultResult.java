package io.doov.core.dsl.impl;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.Metadata;

public class DefaultResult implements Result {

    private final boolean validity;
    private final String message;
    private final List<Metadata> metadatas = new ArrayList<>();

    DefaultResult(boolean validity, String message, List<Metadata> metadatas) {
        this.validity = validity;
        this.message = message;
        this.metadatas.addAll(metadatas);
    }

    @Override
    public boolean isValid() {
        return validity;
    }

    @Override
    public boolean isInvalid() {
        return !validity;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<Metadata> getFailedNodes() {
        return unmodifiableList(metadatas);
    }

}
