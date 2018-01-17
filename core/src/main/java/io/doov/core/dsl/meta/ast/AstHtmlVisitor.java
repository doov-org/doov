/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ElementType.STRING_VALUE;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.*;

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

    private static final List<Operator> operatorList = Arrays.asList(greater_or_equals, greater_than, equals,
                    not_equals, lesser_or_equals, lesser_than);

    private int binaryDeep = 0;
    private boolean endOfSum;
    private final String[] lastLines = new String[3];
    private final OutputStream ops;
    protected final ResourceProvider bundle;
    protected Locale locale;
    private boolean nextBinary;
    protected boolean noLiNary= false;

    public AstHtmlVisitor(OutputStream ops, ResourceProvider bundle, Locale locale) {
        this.ops = ops;
        this.bundle = bundle;
        this.locale = locale;
    }

    // step when

    @Override
    public void startMetadata(StepWhen metadata, int depth) {
        writeWithBuffer(formatNewLine());
        writeWithBuffer(formatCurrentIndent());
    }

    @Override
    public void visitMetadata(StepWhen metadata, int depth) {
        htmlFormatSpan(CSS_CLASS_WHEN, formatWhen());
        writeWithBuffer(formatNewLine());
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(BEG_UL);
        writeWithBuffer(formatNewLine());
    }

    @Override
    public void endMetadata(StepWhen metadata, int depth) {
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(END_UL);
        writeWithBuffer(formatNewLine());
    }

    // field metadata
    @Override
    public void startMetadata(LeafMetadata metadata, int depth) {
        writeWithBuffer(formatCurrentIndent());
        if (!lastLines[0].contains(">" + bundle.get(and, locale) + "<")
                        && !lastLines[0].contains(">" + bundle.get(or, locale) + "<")) {
            if (!endOfSum) {
                writeWithBuffer(BEG_LI);
            }
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
        if (endOfSum) {
            writeWithBuffer("</br>");
            endOfSum = false;
        }
        writeWithBuffer(formatNewLine());
    }

    // binary metadata
    @Override
    public void startMetadata(BinaryMetadata metadata, int depth) {
        if (stackPeek()== MetadataType.BINARY_PREDICATE && nextBinary) {
            writeWithBuffer(formatCurrentIndent());
            writeWithBuffer(BEG_UL);
            writeWithBuffer(formatNewLine());
            binaryDeep++;
            nextBinary=false;
        }

        if (metadata.getRight().type() == MetadataType.BINARY_PREDICATE) {
            nextBinary=true;
        }
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata, int depth) {
        writeWithBuffer(formatCurrentIndent());
        if (metadata.getLeft().type() != MetadataType.NARY_PREDICATE) {
                writeWithBuffer("<br>");
        }
        if (metadata.getRight().type() == MetadataType.NARY_PREDICATE || metadata.getRight().type()==MetadataType.UNARY_PREDICATE) {
            noLiNary=true;
        }

        htmlFormatSpan(CSS_CLASS_BINARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));
        if (operatorList.contains(metadata.getOperator())) {
            endOfSum = true;
        }
        writeWithBuffer(formatNewLine());
    }

    @Override
    public void endMetadata(BinaryMetadata metadata, int depth) {
        if (binaryDeep > 0) {
            writeWithBuffer(formatNewLine());
            writeWithBuffer(formatCurrentIndent());
            writeWithBuffer(END_UL);
            binaryDeep--;
        }
    }

    // nary metadata
    @Override
    public void startMetadata(NaryMetadata metadata, int depth) {
        writeWithBuffer(formatCurrentIndent());
        if (!noLiNary) {
            writeWithBuffer(BEG_LI);
            noLiNary = false;
        }
        writeWithBuffer(formatNewLine());
        writeWithBuffer(formatCurrentIndent());
        htmlFormatSpan(CSS_CLASS_NARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));
        writeWithBuffer(BEG_OL);
        writeWithBuffer(formatNewLine());
    }

    @Override
    public void endMetadata(NaryMetadata metadata, int depth) {
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(END_OL);
        writeWithBuffer(formatNewLine());
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(END_LI);
        writeWithBuffer(formatNewLine());
    }

    // unary metadata
    @Override
    public void visitMetadata(UnaryMetadata metadata, int depth) {
        if (!noLiNary) {
            writeWithBuffer(BEG_LI);
            noLiNary=false;
        }
        htmlFormatSpan(CSS_CLASS_UNARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));
        writeWithBuffer(END_LI);
        writeWithBuffer(BEG_UL);
    }

    @Override
    protected void endMetadata(UnaryMetadata metadata, int depth) {
        writeWithBuffer(END_UL);
    }

    // validation rule
    @Override
    public void startMetadata(ValidationRule metadata, int depth) {
        formatDivStart(CSS_CLASS_VALIDATION_RULE);
        writeWithBuffer(formatNewLine());
        writeWithBuffer(formatCurrentIndent());
    }

    @Override
    public void visitMetadata(ValidationRule metadata, int depth) {
        htmlFormatSpan(CSS_CLASS_VALIDATE, bundle.get(validate_with_message, locale));
        writeWithBuffer(" ");
        htmlFormatSpan(CSS_CLASS_VALIDATION_MESSAGE, formatMessage(metadata));
        writeWithBuffer(formatNewLine());
    }

    @Override
    public void endMetadata(ValidationRule metadata, int depth) {
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(formatNewLine());
        writeWithBuffer(END_DIV);
        writeWithBuffer(formatNewLine());
    }

    // metadata
    @Override
    public void visitMetadata(Metadata metadata, int depth) {
        writeWithBuffer(metadata.readable());
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
        writeWithBuffer("<span class=\"" + cssClass + "\">" + content + "</span> ");
    }

    private void formatDivStart(String cssClass) {
        writeWithBuffer("<div class=\"" + cssClass + "\">");
    }

    protected void writeWithBuffer(String s) {
        lastLines[0] = lastLines[1];
        lastLines[1] = lastLines[2];
        lastLines[2] = s;
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
