/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ElementType.STRING_VALUE;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.WHEN;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Locale;

import io.doov.core.dsl.lang.StepWhen;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.*;

public class AstHtmlVisitor extends AbstractAstVisitor {

    private static final String CSS_CLASS_VALIDATION_MESSAGE = "dsl-validation-message";
    private static final String CSS_CLASS_VALIDATION_RULE = "dsl-validation-rule";
    private static final String CSS_CLASS_VALIDATE = "dsl-token-validate";
    private static final String CSS_CLASS_BINARY = "dsl-token-binary";
    private static final String CSS_CLASS_UNARY = "dsl-token-unary";
    protected static final String CSS_CLASS_NARY = "dsl-token-nary";
    private static final String CSS_CLASS_WHEN = "dsl-token-when";

    private static final String END_DIV = "</div>";

    private static final String BEG_LI = "<li>";
    private static final String END_LI = "</li>";

    private static final String BEG_OL = "<ol>";
    private static final String END_OL = "</ol>";

    private static final String BEG_UL = "<ul>";
    private static final String END_UL = "</ul>";

    private final OutputStream ops;
    protected final ResourceProvider bundle;
    protected Locale locale;
    private boolean closeSum = false;
    private int closeUnaryUL = 0;
    private int insideNary = 0;
    private int nbImbriBinary = 0;
    private boolean rightSideOfBinary = false;
    private int seqBinary = 0;

    public AstHtmlVisitor(OutputStream ops, ResourceProvider bundle, Locale locale) {
        this.ops = ops;
        this.bundle = bundle;
        this.locale = locale;
    }

    // step when

    @Override
    public void startMetadata(StepWhen metadata, int depth) {
        write(BEG_UL);
    }

    @Override
    public void visitMetadata(StepWhen metadata, int depth) {
        htmlFormatSpan(CSS_CLASS_WHEN, formatWhen());
        write(BEG_UL);
    }

    @Override
    public void endMetadata(StepWhen metadata, int depth) {
        write(END_UL);
    }

    // field metadata
    @Override
    public void startMetadata(LeafMetadata metadata, int depth) {
        if (stackPeek() == WHEN || (insideNary > 0 && stackPeek() != BINARY_PREDICATE)) {
            write(BEG_LI);
        }
    }

    @Override
    public void visitMetadata(LeafMetadata leaf, int depth) {
        leaf.stream().forEach(e -> {
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

    @Override
    public void endMetadata(LeafMetadata metadata, int depth) {
        if (stackPeek() == WHEN || (insideNary > 0 && stackPeek() != BINARY_PREDICATE)) {
            write(END_LI);
        }
    }

    // binary metadata
    @Override
    public void startMetadata(BinaryMetadata metadata, int depth) {
        Metadata leftChild = metadata.getLeft();

        if (NARY_PREDICATE == stackPeek() && (metadata.getOperator() != or && metadata.getOperator() != and)) {
            write(BEG_LI);
            closeSum = true;
        }
        if (rightSideOfBinary && leftChild.type() != NARY_PREDICATE) {
            write(BEG_UL);
            nbImbriBinary++;
            rightSideOfBinary = false;
        }
        if (leftChild.type() != BINARY_PREDICATE && leftChild.type() != NARY_PREDICATE) {
            write(BEG_LI);
        } else {
            seqBinary++;
        }
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata, int depth) {
        if (metadata.getOperator() == and || metadata.getOperator() == or) {
            write("<br>");
        }
        htmlFormatSpan(CSS_CLASS_BINARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));
        rightSideOfBinary = true;
    }

    @Override
    public void endMetadata(BinaryMetadata metadata, int depth) {
        if (nbImbriBinary > 0) {
            write(END_UL);
            nbImbriBinary--;
        }
        if (seqBinary == 0) {
            write(END_LI);
        }
        if (seqBinary > 0) {
            seqBinary--;
        }
        if (closeSum) {
            write(END_LI);
            closeSum = true;
        }
        rightSideOfBinary = false;
    }

    // nary metadata
    @Override
    public void startMetadata(NaryMetadata metadata, int depth) {

        if (insideNary == 0 && !rightSideOfBinary && (metadata.getOperator() == sum || metadata.getOperator() ==
                        count || metadata.getOperator() == min)) {
            write(BEG_LI);
        }
        if (stackPeek() == WHEN || stackPeek() != BINARY_PREDICATE) {
            write(BEG_LI);
        }

        htmlFormatSpan(CSS_CLASS_NARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));

        if (insideNary == 0 && !rightSideOfBinary && (metadata.getOperator() == sum || metadata.getOperator() ==
                        count || metadata.getOperator() == min)) {
            write(END_LI);
        }
        if (stackPeek() == WHEN || stackPeek() != BINARY_PREDICATE) {
            write(END_LI);
        }
        rightSideOfBinary = false;
        write(BEG_OL);
        insideNary++;
    }

    @Override
    public void endMetadata(NaryMetadata metadata, int depth) {
        write(END_OL);
        insideNary--;
    }

    // unary metadata
    @Override
    public void visitMetadata(UnaryMetadata metadata, int depth) {
        write(BEG_LI);
        htmlFormatSpan(CSS_CLASS_UNARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));
        write(END_LI);
        if (metadata.children().get(0).type() != MetadataType.LEAF_PREDICATE) {
            write(BEG_UL);
            closeUnaryUL++;
        }
    }

    @Override
    protected void endMetadata(UnaryMetadata metadata, int depth) {
        if (closeUnaryUL > 0) {
            write(END_UL);
            closeUnaryUL--;
        }
    }

    // validation rule
    @Override
    public void startMetadata(ValidationRule metadata, int depth) {
        formatDivStart(CSS_CLASS_VALIDATION_RULE);
    }

    @Override
    public void visitMetadata(ValidationRule metadata, int depth) {
        htmlFormatSpan(CSS_CLASS_VALIDATE, bundle.get(validate_with_message, locale));
        htmlFormatSpan(CSS_CLASS_VALIDATION_MESSAGE, formatMessage(metadata));
    }

    @Override
    public void endMetadata(ValidationRule metadata, int depth) {
        write(END_UL);
        write(END_DIV);
    }

    // metadata
    @Override
    public void visitMetadata(Metadata metadata, int depth) {
        write(metadata.readable());
    }

    // implementation
    protected String formatWhen() {
        return bundle.get(when, locale);
    }

    protected String formatMessage(ValidationRule metadata) {
        return MessageFormat.format("\"{0}\"",
                        metadata.getMessage() == null ? bundle.get(empty, locale) : metadata.getMessage());
    }

    protected void formatLeafOperator(Element e) {
        htmlFormatSpan("dsl-token-operator", bundle.get((Operator) e.getReadable(), locale));
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
            htmlFormatSpan("dsl-token-value", "'" + bundle.get(e.getReadable().readable(), locale) + "'");
        } else {
            htmlFormatSpan("dsl-token-value", bundle.get(e.getReadable().readable(), locale));
        }
    }

    protected void formatLeafUnknown(Element e) {
        htmlFormatSpan("dsl-token-unknown", bundle.get(e.getReadable().readable(), locale));
    }

    protected void htmlFormatSpan(String cssClass, String content) {
        write("<span class=\"" + cssClass + "\">" + content + "</span> ");
    }

    private void formatDivStart(String cssClass) {
        write("<div class=\"" + cssClass + "\">");
    }

    protected void write(String s) {
        try {
            ops.write(s.getBytes("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
