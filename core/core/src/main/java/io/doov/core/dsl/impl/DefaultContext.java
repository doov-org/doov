package io.doov.core.dsl.impl;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class DefaultContext implements Context {

    private final List<Metadata> failed = new ArrayList<>();
    private final boolean shortCircuit;

    public DefaultContext() {
        this(true);
    }

    public DefaultContext(boolean shortCircuit) {
        this.shortCircuit = shortCircuit;
    }

    public List<Metadata> getFailed() {
        return unmodifiableList(failed);
    }

    @Override
    public boolean isShortCircuit() {
        return shortCircuit;
    }

    @Override
    public void failed(Metadata metadata) {
        failed.add(metadata);
    }

}
