/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.ElementType.PARENTHESIS_LEFT;
import static io.doov.core.dsl.meta.ElementType.PARENTHESIS_RIGHT;

import io.doov.core.dsl.lang.Readable;

public class Element {

    private final Readable readable;
    private final ElementType type;

    Element(Readable readable, ElementType type) {
        this.readable = readable;
        this.type = type;
    }

    public Readable getReadable() {
        return readable;
    }

    public ElementType getType() {
        return type;
    }

    @Override
    public String toString() {
        return readable.readable();
    }

    static Element leftParenthesis() {
        return new Element(() -> "(", PARENTHESIS_LEFT);
    }

    static Element rightParenthesis() {
        return new Element(() -> ")", PARENTHESIS_RIGHT);
    }

}
