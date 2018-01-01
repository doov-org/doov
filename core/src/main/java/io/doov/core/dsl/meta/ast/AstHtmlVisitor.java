/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ElementType.OPERATOR;
import static io.doov.core.dsl.meta.i18n.DefaultResourceBundle.BUNDLE;
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
    private static final String CSS_CLASS_NARY = "dsl-token-nary";
    private static final String CSS_CLASS_WHEN = "dsl-token-when";

    private static final String END_DIV = "</div>";

    private static final String BEG_LI = "<li>";
    private static final String END_LI = "</li>";

    private static final String BEG_OL = "<ol>";
    private static final String END_OL = "</ol>";

    private static final String BEG_UL = "<ul>";
    private static final String END_UL = "</ul>";

    private int binaryDeep = 0;
    private boolean closeFieldLI;
    private final String[] lastLines = new String[3];
    private final OutputStream ops;
    private final Locale locale;

    public AstHtmlVisitor(OutputStream ops, Locale locale) {
        this.ops = ops;
        this.locale = locale;
    }

    // step when

    @Override
    public void startMetadata(StepWhen metadata) {
        writeWithBuffer(formatNewLine());
        writeWithBuffer(formatCurrentIndent());
    }

    @Override
    public void visitMetadata(StepWhen metadata) {
        htmlFormatSpan(CSS_CLASS_WHEN, formatWhen());
        writeWithBuffer(formatNewLine());
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(BEG_UL);
        writeWithBuffer(formatNewLine());
    }

    @Override
    public void endMetadata(StepWhen metadata) {
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(END_UL);
        writeWithBuffer(formatNewLine());
    }

    // field metadata
    @Override
    public void startMetadata(LeafMetadata metadata) {
        writeWithBuffer(formatCurrentIndent());
        if (!lastLines[0].contains(">" + BUNDLE.get(and, locale) + "<")
                        && !lastLines[0].contains(">" + BUNDLE.get(or, locale) + "<")) {
            writeWithBuffer(BEG_LI);
            closeFieldLI = true;
        }
    }

    @Override
    public void visitMetadata(LeafMetadata metadata) {
        formatFieldClass(metadata);
    }

    @Override
    public void endMetadata(LeafMetadata metadata) {
        if (closeFieldLI) {
            writeWithBuffer(END_LI);
            closeFieldLI = false;
        }
        writeWithBuffer(formatNewLine());
    }

    // binary metadata
    @Override
    public void startMetadata(BinaryMetadata metadata) {
        if (lastLines[1].contains(">" + BUNDLE.get(and, locale) + "<")
                        || lastLines[1].contains(">" + BUNDLE.get(or, locale) + "<")) {
            writeWithBuffer(formatCurrentIndent());
            writeWithBuffer(BEG_UL);
            writeWithBuffer(formatNewLine());
            binaryDeep++;
        }
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata) {
        writeWithBuffer(formatCurrentIndent());
        htmlFormatSpan(CSS_CLASS_BINARY, escapeHtml4(BUNDLE.get(metadata.getOperator(), locale)));
        writeWithBuffer(formatNewLine());
    }

    @Override
    public void endMetadata(BinaryMetadata metadata) {
        if (binaryDeep > 0) {
            writeWithBuffer(formatNewLine());
            writeWithBuffer(formatCurrentIndent());
            writeWithBuffer(END_UL);
            binaryDeep--;
        }
    }

    // nary metadata
    @Override
    public void startMetadata(NaryMetadata metadata) {
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(BEG_LI);
        writeWithBuffer(formatNewLine());
        writeWithBuffer(formatCurrentIndent());
        htmlFormatSpan(CSS_CLASS_NARY, escapeHtml4(BUNDLE.get(metadata.getOperator(), locale)));
        writeWithBuffer(BEG_OL);
        writeWithBuffer(formatNewLine());
    }

    @Override
    public void endMetadata(NaryMetadata metadata) {
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(END_OL);
        writeWithBuffer(formatNewLine());
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(END_LI);
        writeWithBuffer(formatNewLine());
    }

    // nary metadata
    @Override
    public void visitMetadata(UnaryMetadata metadata) {
        writeWithBuffer(BEG_LI);
        htmlFormatSpan(CSS_CLASS_UNARY, escapeHtml4(BUNDLE.get(metadata.getOperator(), locale)));
        writeWithBuffer(END_LI);
        writeWithBuffer(BEG_UL);
    }

    // validation rule
    @Override
    public void startMetadata(ValidationRule metadata) {
        formatDivStart(CSS_CLASS_VALIDATION_RULE);
        writeWithBuffer(formatNewLine());
        writeWithBuffer(formatCurrentIndent());
    }

    @Override
    public void visitMetadata(ValidationRule metadata) {
        htmlFormatSpan(CSS_CLASS_VALIDATE, BUNDLE.get(validate_with_message, locale));
        writeWithBuffer(" ");
        htmlFormatSpan(CSS_CLASS_VALIDATION_MESSAGE, formatMessage(metadata));
        writeWithBuffer(formatNewLine());
    }

    @Override
    public void endMetadata(ValidationRule metadata) {
        writeWithBuffer(formatCurrentIndent());
        writeWithBuffer(formatNewLine());
        writeWithBuffer(END_DIV);
        writeWithBuffer(formatNewLine());
    }

    // metadata
    @Override
    public void visitMetadata(Metadata metadata) {
        writeWithBuffer(metadata.readable());
    }

    // implementation
    protected String formatWhen() {
        return BUNDLE.get(when, locale);
    }

    protected String formatMessage(ValidationRule metadata) {
        return MessageFormat.format("\"{0}\"", metadata.getMessage() == null ? BUNDLE.get(empty, locale) : metadata.getMessage());
    }

    // format
    protected void formatFieldClass(LeafMetadata field) {
        field.stream().forEach(element -> {
            if (element.getType() == OPERATOR)
                htmlFormatSpan(getCssClass(element), BUNDLE.get((Operator) element.getReadable(), locale));
            else
                htmlFormatSpan(getCssClass(element), element.getReadable().readable());
        });
    }

    protected String getCssClass(Element element) {
        switch (element.getType()) {
            case FIELD:
                return "dsl-token-field";
            case OPERATOR:
                return "dsl-token-operator";
            case VALUE:
                return "dsl-token-value";
            case UNKNOWN:
                return "dsl-token-unknown";
            default:
                throw new IllegalArgumentException("Unknown css class for element " + element);
        }
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
}
