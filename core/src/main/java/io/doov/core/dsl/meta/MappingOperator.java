/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

public enum MappingOperator implements Operator {
    // other
    fields("fields"),
    and("and"),
    then("then"),
    _else("else"),

    // mappings
    map("map"), //
    mappings("mappings"), //
    to("to"), //
    using("using"),//
    ; //
    private final String readable;

    MappingOperator(String readable) {
        this.readable = readable;
    }

    @Override
    public String readable() {
        return readable;
    }
}
