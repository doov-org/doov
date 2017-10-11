package org.modelmap.core.dsl.impl;

import org.modelmap.core.dsl.lang.EValidity;
import org.modelmap.core.dsl.lang.Result;

public class DefaultResult implements Result {

    private final EValidity validity;
    private final String message;

    public DefaultResult(EValidity validity, String message) {
        this.validity = validity;
        this.message = message;
    }

    @Override
    public EValidity validity() {
        return validity;
    }

    @Override
    public String message() {
        return message;
    }

}
