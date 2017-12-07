/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static java.util.stream.Collectors.joining;

import java.text.MessageFormat;

import io.doov.core.dsl.lang.StepWhen;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.*;

public class AstHtmlVisitor extends AstTextVisitor {

    private static final String CSS_CLASS_VALIDATION_MESSAGE = "dsl-validation-message";
    private static final String CSS_CLASS_VALIDATION_RULE = "dsl-validation-rule";
    private static final String CSS_CLASS_BINARY = "dsl-token-binary";
    private static final String CSS_CLASS_NARY = "dsl-token-nary";
    private static final String CSS_CLASS_RULE = "dsl-token-rule";
    private static final String CSS_CLASS_WHEN = "dsl-token-when";

    private static final String END_DIV = "</div>";

    private static final String BEG_LI = "<li>";
    private static final String END_LI = "</li>";

    private static final String BEG_UL = "<ul>";
    private static final String END_UL = "</ul>";

    private int binaryDeep = 0;

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
        sb.append(formatSpan(CSS_CLASS_WHEN, formatWhen()));
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
        sb.append(BEG_LI);
    }

    @Override
    public void visitMetadata(FieldMetadata metadata) {
        sb.append(formatFieldClass(metadata));
    }

    @Override
    public void endMetadata(FieldMetadata metadata) {
        sb.append(END_LI);
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
        sb.append(BEG_LI);
        sb.append(formatSpan(CSS_CLASS_BINARY, metadata.getOperator()));
        sb.append(END_LI);
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
        sb.append(formatSpan(CSS_CLASS_NARY, metadata.getOperator()));
        sb.append(BEG_UL);
        sb.append(formatNewLine());
    }

    @Override
    public void endMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append(END_UL);
        sb.append(formatNewLine());
        sb.append(formatCurrentIndent());
        sb.append(END_LI);
        sb.append(formatNewLine());
    }

    // validation rule

    @Override
    public void startMetadata(ValidationRule metadata) {
        sb.append(formatDivStart(CSS_CLASS_VALIDATION_RULE));
        sb.append(formatNewLine());
        sb.append(BEG_UL);
        sb.append(formatNewLine());
        sb.append(formatCurrentIndent());
        sb.append(BEG_LI);
        sb.append(formatSpan(CSS_CLASS_RULE, formatRule()));
        sb.append(formatNewLine());
    }

    @Override
    public void visitMetadata(ValidationRule metadata) {
        sb.append(BEG_LI);
        sb.append(formatValidateWithMessage());
        sb.append(" ");
        sb.append(formatSpan(CSS_CLASS_VALIDATION_MESSAGE, formatMessage(metadata)));
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
        return metadata.getMessage() == null ? "empty" : metadata.getMessage();
    }

    // format

    private String formatFieldClass(FieldMetadata field) {
        return field.stream()
                .map(element -> formatSpan(getCssClass(element), element.getReadable().readable()))
                .collect(joining(" "));
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

    private String formatSpan(String cssClass, String content) {
        return MessageFormat.format("<span class=''{0}''>{1}</span>", cssClass, content);
    }

    private String formatDivStart(String cssClass) {
        return MessageFormat.format("<div class=''{0}''>", cssClass);
    }

}
