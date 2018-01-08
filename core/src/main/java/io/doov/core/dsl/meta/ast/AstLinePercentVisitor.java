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

    public AstLinePercentVisitor(StringBuilder stringBuilder, ResourceProvider bundle, Locale locale) {
        super(stringBuilder, bundle, locale);
    }

    @Override
    public void startMetadata(StepWhen metadata, int depth) {
        sb.append(percentage((PredicateMetadata) metadata.stepCondition().getMetadata())+ " ");
        super.startMetadata(metadata, depth);
    }

    @Override
    public void startMetadata(NaryMetadata metadata, int depth) {
        sb.append(formatCurrentIndent());
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
        sb.append(percentage(metadata) +" ");
        sb.append("[");
    }

    @Override
    protected String formatLeafMetadata(LeafMetadata metadata) {
        return super.formatLeafMetadata(metadata) +" "+ percentage(metadata);
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
