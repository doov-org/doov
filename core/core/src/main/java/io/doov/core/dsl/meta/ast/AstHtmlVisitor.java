/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import java.text.MessageFormat;
import java.util.Arrays;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.*;

public class AstHtmlVisitor extends AstTextVisitor {

    private int binaryDeep = 0;

    private static final String BEG_LI = "<li>";
    private static final String BEG_UL = "<ul>";
    private static final String END_LI = "</li>";
    private static final String END_UL = "</ul>";

    public AstHtmlVisitor(StringBuilder stringBuilder) {
        super(stringBuilder);
    }

    //    StepWhen

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
        sb.append(formatWhen());
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

    //    FIELDMETA

    @Override
    public void startMetadata(FieldMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append(BEG_LI);
    }

    @Override
    public void visitMetadata(FieldMetadata metadata) {
        sb.append(formatFieldMetadata(metadata));
    }

    @Override
    public void endMetadata(FieldMetadata metadata) {
        sb.append(END_LI);
        sb.append(formatNewLine());
    }

    // Unary

    // Binary

    @Override
    public void startMetadata(BinaryMetadata metadata) {

        String[] split = sb.toString().split("\n");

        // int hardcoder en fonction de l'utilisation de formatnewline
        if (split[split.length - 1].contains("and") || split[split.length - 1].contains("or")) {
            binaryDeep++;
            sb.append(formatCurrentIndent());
            sb.append(BEG_UL);
            sb.append(formatNewLine());
        }
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append(formatOperator(metadata));
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

    // nary

    @Override
    public void startMetadata(NaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append(BEG_LI);
        sb.append(formatNewLine());
        sb.append(formatCurrentIndent());
        sb.append(formatOperator(metadata));
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

    // Validation Rule

    @Override
    public void startMetadata(ValidationRule metadata) {
        sb.append("<div class='VALIDATIONRULE' id=''>");
        sb.append(formatNewLine());
        sb.append(BEG_UL);
        sb.append(formatNewLine());
        sb.append(formatCurrentIndent());
        sb.append(BEG_LI);
        sb.append(formatRule());
        sb.append(formatNewLine());
    }

    @Override
    public void visitMetadata(ValidationRule metadata) {
        sb.append(BEG_LI);
        sb.append(formatValidateWithMessage());
        sb.append(" ");
        sb.append(formatMessage(metadata));
        sb.append(END_LI);
        sb.append(END_UL);
        sb.append(formatNewLine());
    }

    @Override
    public void endMetadata(ValidationRule metadata) {
        sb.append(formatCurrentIndent());
        sb.append(END_UL);
        sb.append(formatNewLine());
        sb.append("</div>");
        sb.append(formatNewLine());
    }

    // Metadata

    @Override
    public void visitMetadata(Metadata metadata) {
        sb.append(formatOperator(metadata));
    }

    @Override
    protected String formatField(Readable field) {
        return (null == field) ? null : MessageFormat
                        .format("<span class=''{0}'' id=''{1}''>{2}</span>", "PLACEHOLDER", "", field.readable());
    }

    @Override
    protected String formatOperator(Readable operator) {
        return "<span class='OPERATOR' id=''>" + operator.readable() + "</span>";
    }


}
