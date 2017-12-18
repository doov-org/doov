/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import java.text.MessageFormat;

import io.doov.core.dsl.lang.StepWhen;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.*;

public class AstHtmlVisitor extends AstTextVisitor {

    private static final String CSS_CLASS_VALIDATION_MESSAGE = "dsl-validation-message";
    private static final String CSS_CLASS_VALIDATION_RULE = "dsl-validation-rule";
    private static final String CSS_CLASS_VALIDATE = "dsl-token-validate";
    private static final String CSS_CLASS_BINARY = "dsl-token-binary";
    private static final String CSS_CLASS_UNARY = "dsl-token-unary";
    private static final String CSS_CLASS_NARY = "dsl-token-nary";
    private static final String CSS_CLASS_RULE = "dsl-token-rule";
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

    public AstHtmlVisitor(StringBuilder stringBuilder) {
        super(stringBuilder);
    }

    // step when

    @Override
    public void startMetadata(StepWhen metadata) {
        sb.append(formatCurrentIndent());
        sb.append(BEG_UL);
        sb.append(formatNewLine());
        sb.append(formatCurrentIndent());
        sb.append(BEG_LI);
    }

    @Override
    public void visitMetadata(StepWhen metadata) {
        HtmlFormatSpan(CSS_CLASS_WHEN, formatWhen(), sb);
        sb.append(END_LI);
        sb.append(formatNewLine());
        sb.append(formatCurrentIndent());
        sb.append(BEG_UL);
        sb.append(formatNewLine());
    }

    @Override
    public void endMetadata(StepWhen metadata) {
        sb.append(formatCurrentIndent());
        sb.append(END_UL);
        sb.append(formatNewLine());
    }

    // field metadata

    @Override
    public void startMetadata(FieldMetadata metadata) {
        sb.append(formatCurrentIndent());
        String[] split = sb.toString().split("\n");
        if (!split[split.length - 2].contains(">and<") && !split[split.length - 2].contains(">or<")) {
            sb.append(BEG_LI);
            closeFieldLI = true;
        }
    }

    @Override
    public void visitMetadata(FieldMetadata metadata) {
        formatFieldClass(metadata, sb);
    }

    @Override
    public void endMetadata(FieldMetadata metadata) {
        if (closeFieldLI) {
            sb.append(END_LI);
            closeFieldLI = false;
        }
        sb.append(formatNewLine());
    }

    // binary metadata

    @Override
    public void startMetadata(BinaryMetadata metadata) {
        String[] split = sb.toString().split("\n");
        if (split[split.length - 1].contains(">and<") || split[split.length - 1].contains(">or<")) {
            sb.append(formatCurrentIndent());
            sb.append(BEG_UL);
            sb.append(formatNewLine());
            binaryDeep++;
        }
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        HtmlFormatSpan(CSS_CLASS_BINARY, metadata.getOperator().readable(), sb);
        sb.append(formatNewLine());
    }

    @Override
    public void endMetadata(BinaryMetadata metadata) {
        if (binaryDeep > 0) {
            sb.append(formatNewLine());
            sb.append(formatCurrentIndent());
            sb.append(END_UL);
            binaryDeep--;
        }
    }

    // nary metadata

    @Override
    public void startMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append(BEG_LI);
        sb.append(formatNewLine());
        sb.append(formatCurrentIndent());
        HtmlFormatSpan(CSS_CLASS_NARY, metadata.getOperator().readable(), sb);
        sb.append(BEG_OL);
        sb.append(formatNewLine());
    }

    @Override
    public void endMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append(END_OL);
        sb.append(formatNewLine());
        sb.append(formatCurrentIndent());
        sb.append(END_LI);
        sb.append(formatNewLine());
    }

    // nary metadata

    @Override
    public void visitMetadata(UnaryMetadata metadata) {
        sb.append(BEG_LI);
        HtmlFormatSpan(CSS_CLASS_UNARY, metadata.getOperator().readable(), sb);
        sb.append(END_LI);
        sb.append(BEG_UL);
    }

    // validation rule

    @Override
    public void startMetadata(ValidationRule metadata) {
        formatDivStart(CSS_CLASS_VALIDATION_RULE, sb);
        sb.append(formatNewLine());
        sb.append(BEG_UL);
        sb.append(formatNewLine());
        sb.append(formatCurrentIndent());
        sb.append(BEG_LI);
        HtmlFormatSpan(CSS_CLASS_RULE, formatRule(), sb);
        sb.append(formatNewLine());
    }

    @Override
    public void visitMetadata(ValidationRule metadata) {
        sb.append(BEG_LI);
        HtmlFormatSpan(CSS_CLASS_VALIDATE, formatValidateWithMessage(), sb);
        sb.append(" ");
        HtmlFormatSpan(CSS_CLASS_VALIDATION_MESSAGE, formatMessage(metadata), sb);
        sb.append(END_LI);
        sb.append(END_UL);
        sb.append(formatNewLine());
    }

    @Override
    public void endMetadata(ValidationRule metadata) {
        sb.append(formatCurrentIndent());
        sb.append(END_UL);
        sb.append(formatNewLine());
        sb.append(END_DIV);
        sb.append(formatNewLine());
    }

    // metadata

    @Override
    public void visitMetadata(Metadata metadata) {
        sb.append(formatOperator(metadata));
    }

    // implementation

    @Override
    protected String formatWhen() {
        return "When";
    }

    @Override
    protected String formatMessage(ValidationRule metadata) {
        return MessageFormat.format("\"{0}\"", metadata.getMessage() == null ? "empty" : metadata.getMessage());
    }

    // format

    private void formatFieldClass(FieldMetadata field, StringBuilder sb) {
        field.stream().forEach(element -> HtmlFormatSpan(getCssClass(element), element.getReadable().readable(), sb));
    }

    private String getCssClass(FieldMetadata.Element element) {
        switch (element.getType()) {
            case field:
                return "dsl-token-field";
            case operator:
                return "dsl-token-operator";
            case value:
                return "dsl-token-value";
            case unknown:
                return "dsl-token-unknown";
            default:
                throw new IllegalArgumentException("Unknown css class for element " + element);
        }
    }

    private void HtmlFormatSpan(String cssClass, String content, StringBuilder sb) {
        sb.append("<span class=\"").append(cssClass).append("\">");
        sb.append(content).append("</span>");
    }

    private void formatDivStart(String cssClass, StringBuilder sb) {
        sb.append("<div class=\"").append(cssClass).append("\">");
    }

}
