/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.count;
import static io.doov.core.dsl.meta.DefaultOperator.sum;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static java.util.stream.Collectors.toList;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import io.doov.core.dsl.meta.predicate.*;

public class AstLinePercentVisitor extends AstLineVisitor {

    private static final NumberFormat formatter = new DecimalFormat("###.#");

    public AstLinePercentVisitor(StringBuilder stringBuilder, ResourceProvider bundle, Locale locale) {
        super(stringBuilder, bundle, locale);
    }

    @Override
    public void startWhen(Metadata metadata, int depth) {
        sb.append(percentage((PredicateMetadata) metadata.children().findFirst().orElse(null)) + " ");
        super.startWhen(metadata, depth);
    }

    @Override
    public void startBinary(BinaryPredicateMetadata metadata, int depth) {
        super.startBinary(metadata, depth);
        if (metadata.children().collect(toList()).get(0).type() == NARY_PREDICATE) {
            sb.append(percentage(metadata));
        }
    }

    @Override
    public void startNary(NaryPredicateMetadata metadata, int depth) {
        if (metadata.getOperator() != count && metadata.getOperator() != sum) {
            sb.append(percentage(metadata));
        }
        sb.append(formatCurrentIndent());
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
        sb.append("[");
    }

    @Override
    protected String formatLeafMetadata(LeafMetadata<?> metadata) {
        if (stackPeek() == BINARY_PREDICATE) {
            return super.formatLeafMetadata(metadata);
        }
        return (metadata instanceof PredicateMetadata) ? percentage((PredicateMetadata) metadata) : ""
                + super.formatLeafMetadata(metadata);
    }

    private String percentage(PredicateMetadata metadata) {
        int t = metadata.trueEvalCount();
        int f = metadata.falseEvalCount();

        if (f == 0 && t == 0) {
            return "[n/a]";
        } else {
            return "[" + formatter.format((t / ((double) t + f)) * 100) + "]";
        }
    }
}
