/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import io.doov.core.dsl.lang.StepWhen;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.FieldMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.NaryMetadata;
import io.doov.core.dsl.meta.UnaryMetadata;

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

    public AstHtmlVisitor(OutputStream ops) {
        this.ops = ops;
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
    public void startMetadata(FieldMetadata metadata) {
        writeWithBuffer(formatCurrentIndent());
        if (!lastLines[0].contains(">and<") && !lastLines[0].contains(">or<")) {
            writeWithBuffer(BEG_LI);
            closeFieldLI = true;
        }
    }

    @Override
    public void visitMetadata(FieldMetadata metadata) {
        formatFieldClass(metadata);
    }

    @Override
    public void endMetadata(FieldMetadata metadata) {
        if (closeFieldLI) {
            writeWithBuffer(END_LI);
            closeFieldLI = false;
        }
        writeWithBuffer(formatNewLine());
    }

    // binary metadata
    @Override
    public void startMetadata(BinaryMetadata metadata) {
        if (lastLines[1].contains(">and<") || lastLines[1].contains(">or<")) {
            writeWithBuffer(formatCurrentIndent());
            writeWithBuffer(BEG_UL);
            writeWithBuffer(formatNewLine());
            binaryDeep++;
        }
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata) {
        writeWithBuffer(formatCurrentIndent());
        htmlFormatSpan(CSS_CLASS_BINARY, escapeHtml4(metadata.getOperator().readable()));
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
        htmlFormatSpan(CSS_CLASS_NARY, escapeHtml4(metadata.getOperator().readable()));
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
        htmlFormatSpan(CSS_CLASS_UNARY, escapeHtml4(metadata.getOperator().readable()));
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
        htmlFormatSpan(CSS_CLASS_VALIDATE, "validate with message");
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
        return "when";
    }

    protected String formatMessage(ValidationRule metadata) {
        return MessageFormat.format("\"{0}\"", metadata.getMessage() == null ? "empty" : metadata.getMessage());
    }

    // format
    protected void formatFieldClass(FieldMetadata field) {
        field.stream().forEach(element -> htmlFormatSpan(getCssClass(element), element.getReadable().readable()));
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
