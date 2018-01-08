/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import io.doov.core.dsl.lang.StepWhen;
import io.doov.core.dsl.meta.*;

public class AstLinePercentVisitor extends AstLineVisitor {
    private static final NumberFormat formatter = new DecimalFormat("###.#");
    private boolean endNary;

    public AstLinePercentVisitor(StringBuilder stringBuilder, ResourceProvider bundle, Locale locale) {
        super(stringBuilder, bundle, locale);
    }

    @Override
    public void startMetadata(StepWhen metadata, int depth) {
        sb.append(percentage((PredicateMetadata) metadata.stepCondition().getMetadata())+ " ");
        super.startMetadata(metadata, depth);
    }

    @Override
    public void startMetadata(BinaryMetadata metadata, int depth) {
        super.startMetadata(metadata, depth);
        if (metadata.children().get(0).type() == MetadataType.NARY_PREDICATE) {
            sb.append(percentage(metadata));
        }
    }

    @Override
    public void startMetadata(NaryMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
        if (metadata.getOperator() != DefaultOperator.count && metadata.getOperator() != DefaultOperator.count) {
            sb.append(percentage(metadata));
        }
        sb.append("[");
    }

    @Override
    protected void endMetadata(NaryMetadata metadata, int depth) {
        super.endMetadata(metadata, depth);
        endNary = true;
    }

    @Override
    protected String formatLeafMetadata(LeafMetadata metadata) {
        if (stackPeek() == MetadataType.BINARY_PREDICATE) {
            return super.formatLeafMetadata(metadata);
        }
        return percentage(metadata) + super.formatLeafMetadata(metadata);
    }

    private String percentage(PredicateMetadata metadata) {
        int t = metadata.trueEvalCount();
        int f = metadata.falseEvalCount();

        if (f == 0 && t == 0) {
            return "[n/a]";
        }
        else{
            return "[" + formatter.format((t / ((double) t + f))*100) + "]";
        }
    }
}
