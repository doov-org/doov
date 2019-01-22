/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ElementType.STRING_VALUE;
import static io.doov.core.dsl.meta.MetadataType.*;
import static io.doov.core.dsl.meta.ast.AstHtmlRenderer.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import io.doov.core.dsl.meta.predicate.*;

public class AstHtmlVisitor extends AbstractAstVisitor {

    protected final OutputStream ops;
    protected final ResourceProvider bundle;
    protected Locale locale;

    public AstHtmlVisitor(OutputStream ops, ResourceProvider bundle, Locale locale) {
        this.ops = ops;
        this.bundle = bundle;
        this.locale = locale;
    }

    private long closeUnaryUL() {
        return stackSteam().map(Metadata::type).filter(t -> t == UNARY_PREDICATE).count();
    }

    private boolean rightSideOfBinary(Metadata metadata) {
        return BINARY_PREDICATE == stackPeekType() && stackPeek().childAt(1) == metadata;
    }

    private long nbImbriBinary() {
        return stackSteam().map(Metadata::getOperator).filter(op -> op == and || op == or).count();
    }

    private boolean isNextLeafPredicate() {
        return stackPeek().type() != BINARY_PREDICATE
                        || stackPeek().getOperator() == or
                        || stackPeek().getOperator() == and;
    }

    private boolean insideNary() {
        return stackSteam().map(Metadata::type).filter(t -> t == NARY_PREDICATE).findFirst().isPresent();
    }

    private final boolean insideSum() {
        return stackSteam().map(Metadata::getOperator).filter(op -> op == sum || op == min).findFirst().isPresent();
    }

    // step when

    @Override
    public void startWhen(Metadata metadata, int depth) {
        htmlFormatSpan(CSS_CLASS_WHEN, formatWhen());
        write(beginUl(CSS_CLASS_UL_WHEN));
    }

    @Override
    public void beforeChildWhen(Metadata metadata, Metadata child, int depth) {
        if (isLeaf(child.type())) {
            write(beginLi(CSS_CLASS_LI_LEAF));
        }
    }

    @Override
    public void afterChildWhen(Metadata metadata, Metadata child, boolean hasNext, int depth) {
        if (isLeaf(child.type())) {
            write(endLi());
        }
    }

    @Override
    public void endWhen(Metadata metadata, int depth) {
        write(endUl());
    }

    // field metadata

    @Override
    public void startLeaf(LeafMetadata<?> leaf, int depth) {
        if (!insideSum()) {
            if (isNextLeafPredicate() && leaf.type() == LEAF_PREDICATE) {
                write(exclusionBar((PredicateMetadata) leaf, ExclusionBar.SMALL, locale));
            }
        }
        leaf.elements().stream().forEach(e -> {
            switch (e.getType()) {
                case PARENTHESIS_LEFT:
                case PARENTHESIS_RIGHT:
                    formatParenthesis(e);
                    break;
                case OPERATOR:
                    formatLeafOperator(e);
                    break;
                case TEMPORAL_UNIT:
                    formatTemporalUnit(e);
                    break;
                case FIELD:
                    formatLeafField(e);
                    break;
                case VALUE:
                case STRING_VALUE:
                    formatLeafValue(e);
                    break;
                case UNKNOWN:
                    formatLeafUnknown(e);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown element type " + e.getType());
            }
        });
    }

    // binary metadata
    @Override
    public void startBinary(BinaryPredicateMetadata metadata, int depth) {
        Metadata leftChild = metadata.getLeft();

        if (NARY_PREDICATE == stackPeekType() && (metadata.getOperator() != or && metadata.getOperator() != and)) {
            write(beginLi(CSS_CLASS_LI_BINARY));
        } else if (leftChild.type() != BINARY_PREDICATE && leftChild.type() != NARY_PREDICATE) {
            write(beginLi(CSS_CLASS_LI_BINARY));
        }
        if (rightSideOfBinary(metadata) && leftChild.type() != NARY_PREDICATE) {
            write(beginUl(CSS_CLASS_UL_BINARY));
        }
        if ((metadata.getOperator() != and && metadata.getOperator() != or) && stackPeekType() != UNARY_PREDICATE
                        && !isFunctionOperator(metadata.getOperator())) {
            write(exclusionBar(metadata, ExclusionBar.BIG, locale));
        }
    }

    @Override
    public void afterChildBinary(BinaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
        if (hasNext) {
            if (metadata.getOperator() == and || metadata.getOperator() == or) {
                write("<br>");
            }
            htmlFormatSpan(CSS_CLASS_BINARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));

            if (metadata.getRight().type() == UNARY_PREDICATE) {
                write(beginUl(CSS_CLASS_UL_BINARY_CHILD));
            }
        }
    }

    @Override
    public void endBinary(BinaryPredicateMetadata metadata, int depth) {
        if (nbImbriBinary() > 0) {
            write(endUl());
        }
        write(endLi());
    }

    // nary metadata
    @Override
    public void startNary(NaryPredicateMetadata metadata, int depth) {
        if (!insideNary() && !rightSideOfBinary(metadata) && (metadata.getOperator() == sum
                        || metadata.getOperator() == count || metadata.getOperator() == min)) {
            write(beginLi(CSS_CLASS_LI_NARY));
        }
        if (stackPeekType() == WHEN || stackPeekType() != BINARY_PREDICATE) {
            write(beginLi(CSS_CLASS_LI_NARY));
        }

        if (metadata.getOperator() != count && metadata.getOperator() != sum && metadata.getOperator() != min) {
            write(exclusionBar(metadata, ExclusionBar.BIG, locale));
        }
        htmlFormatSpan(CSS_CLASS_NARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));

        write(beginOl(CSS_CLASS_OL_NARY));
    }

    @Override
    public void beforeChildNary(NaryPredicateMetadata metadata, Metadata child, int depth) {
        if (isLeaf(child.type())) {
            write(beginLi(CSS_CLASS_LI_LEAF));
        }
    }

    @Override
    public void afterChildNary(NaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
        if (isLeaf(child.type())) {
            write(endLi());
        }
    }

    @Override
    public void endNary(NaryPredicateMetadata metadata, int depth) {
        write(endOl());
    }

    // unary metadata
    @Override
    public void startUnary(UnaryPredicateMetadata metadata, int depth) {
        write(beginLi(CSS_CLASS_LI_UNARY));
        MetadataType childType = metadata.childAt(0).type();
        if (!isLeaf(childType)) {
            htmlFormatSpan(CSS_CLASS_UNARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));
        }
        if (!isLeaf(childType)) {
            write(beginUl(CSS_CLASS_UL_UNARY));
        }
    }

    @Override
    public void endUnary(UnaryPredicateMetadata metadata, int depth) {
        MetadataType childType = metadata.childAt(0).type();
        if (isLeaf(childType)) {
            htmlFormatSpan(CSS_CLASS_UNARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));
        }
        write(exclusionBar(metadata, ExclusionBar.SMALL, locale));
        if (closeUnaryUL() > 0) {
            write(endUl());
        }
        write(endLi());
    }

    // validation rule

    @Override
    public void startRule(Metadata metadata, int depth) {
        formatDivStart(CSS_CLASS_VALIDATION_RULE);
    }

    @Override
    public void endRule(Metadata metadata, int depth) {
        htmlFormatSpan(CSS_CLASS_VALIDATE, bundle.get(validate, locale));
        write(endDiv());
    }

    // metadata

    @Override
    public void afterChildDefault(Metadata metadata, Metadata child, boolean hasNext, int depth) {
        write(metadata.readable());
    }

    // implementation
    protected String formatWhen() {
        return bundle.get(when, locale);
    }

    protected void formatLeafOperator(Element e) {
        htmlFormatSpan("dsl-token-operator", escapeHtml4(bundle.get((Operator) e.getReadable(), locale)));
    }

    protected void formatTemporalUnit(Element e) {
        htmlFormatSpan("dsl-token-operator", bundle.get(e.getReadable().readable(), locale));
    }

    protected void formatLeafField(Element e) {
        htmlFormatSpan("dsl-token-field", e.getReadable().readable());
    }

    protected void formatParenthesis(Element e) {
        htmlFormatSpan("dsl-token-parenthesis", e.getReadable().readable());
    }

    protected void formatLeafValue(Element e) {
        if (e.getType() == STRING_VALUE) {
            htmlFormatSpan("dsl-token-value", "'" + escapeHtml4(bundle.get(e.getReadable().readable(), locale)) + "'");
        } else {
            htmlFormatSpan("dsl-token-value", escapeHtml4(bundle.get(e.getReadable().readable(), locale)));
        }
    }

    protected void formatLeafUnknown(Element e) {
        htmlFormatSpan("dsl-token-unknown", escapeHtml4(bundle.get(e.getReadable().readable(), locale)));
    }

    protected void htmlFormatSpan(String cssClass, String content) {
        write("<span class=\"" + cssClass + "\">" + content + "</span> ");
    }

    private void formatDivStart(String cssClass) {
        write("<div class=\"" + cssClass + "\">");
    }

    protected void write(String s) {
        try {
            ops.write(s.getBytes(UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    private boolean isLeaf(MetadataType childType) {
        return childType == LEAF_PREDICATE || childType == LEAF_VALUE || childType == FIELD_PREDICATE;
    }

    private boolean isFunctionOperator(Operator operator) {
        return operator == times || operator == as_string || operator == as || operator == as_a_number
                        || operator == plus || operator == minus || operator == today_plus || operator == today_minus
                        || operator == age_at;
    }
}
