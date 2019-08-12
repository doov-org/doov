/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast;

import java.util.ArrayDeque;

import io.doov.core.dsl.meta.Metadata;
import io.doov.ts.ast.writer.TypeScriptWriter;

public class AstTSRenderer {

    private final TypeScriptWriter writer;

    public AstTSRenderer(TypeScriptWriter writer) {
        this.writer = writer;
    }

    public void toTS(Metadata metadata) {
        toTS(metadata, new ArrayDeque<>());
    }

    private void toTS(Metadata metadata, ArrayDeque<Metadata> parents) {
        parents.push(metadata);
        try {
            switch (metadata.type()) {
                case RULE:
                    rule(metadata, parents);
                    break;
                case WHEN:
                    when(metadata, parents);
                    break;
                case BINARY_PREDICATE:
                case TEMPLATE_PARAM:
                    binary(metadata, parents);
                    break;
                case LEAF_PREDICATE:
                case FIELD_PREDICATE:
                case LEAF_VALUE:
                case MAPPING_LEAF:
                case TEMPLATE_IDENTIFIER:
                    leaf(metadata, parents);
                    break;
                case MAPPING_LEAF_ITERABLE:
                    iterable(metadata, parents);
                    break;
                case TYPE_CONVERTER:
                    typeConverter(metadata, parents);
                    break;
                case UNARY_PREDICATE:
                    unary(metadata, parents);
                    break;
                case NARY_PREDICATE:
                case MULTIPLE_MAPPING:
                case THEN_MAPPING:
                case ELSE_MAPPING:
                    nary(metadata, parents);
                    break;
                case MAPPING_INPUT:
                    mappingInput(metadata, parents);
                    break;
                case FIELD_PREDICATE_MATCH_ANY:
                    fieldMatchAny(metadata, parents);
                    break;
                case SINGLE_MAPPING:
                    singleMapping(metadata, parents);
                    break;
                default:
                    throw new IllegalStateException(metadata.type().name());
            }
        } finally {
            parents.pop();
        }
    }

    private void rule(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

    private void when(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

    private void binary(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

    private void leaf(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

    private void iterable(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

    private void typeConverter(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

    private void unary(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

    private void nary(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

    private void mappingInput(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

    private void fieldMatchAny(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

    private void singleMapping(Metadata metadata, ArrayDeque<Metadata> parents) {

    }

}
