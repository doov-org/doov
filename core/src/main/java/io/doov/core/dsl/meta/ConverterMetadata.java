/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta;

import static io.doov.core.dsl.meta.ElementType.STRING_VALUE;
import static io.doov.core.dsl.meta.MetadataType.TYPE_CONVERTER;
import static io.doov.core.dsl.meta.MetadataType.TYPE_CONVERTER_IDENTITY;

import java.util.*;

import io.doov.core.dsl.lang.*;

public class ConverterMetadata implements Metadata {

    private final Element element;
    private final MetadataType type;

    public ConverterMetadata(Element element, MetadataType type) {
        this.element = element;
        this.type = type;
    }

    public static ConverterMetadata metadata(String description) {
        return new ConverterMetadata(new Element(() -> Optional.ofNullable(description)
                        .filter(d -> !d.isEmpty())
                        .orElse("-function-"), STRING_VALUE), TYPE_CONVERTER);
    }

    public static ConverterMetadata identity() {
        return new ConverterMetadata(new Element(() -> "identity", STRING_VALUE), TYPE_CONVERTER_IDENTITY);
    }

    @Override
    public void accept(MetadataVisitor visitor, int depth) {
        if (type != MetadataType.TYPE_CONVERTER_IDENTITY) {
            visitor.start(this, depth);
            visitor.visit(this, depth);
            visitor.end(this, depth);
        }
    }

    @Override
    public List<Element> flatten() {
        return Collections.singletonList(element);
    }

    @Override
    public List<Metadata> children() {
        return Collections.emptyList();
    }

    @Override
    public String readable(Locale locale) {
        return element.getReadable().readable();
    }

    @Override
    public MetadataType type() {
        return type;
    }

    @Override
    public Metadata message(Context context) {
        return this;
    }
}
