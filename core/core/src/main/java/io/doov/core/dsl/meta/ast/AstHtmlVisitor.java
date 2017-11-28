/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.*;

public class AstHtmlVisitor extends AstTextVisitor{

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
        sb.append("when");
        sb.append(END_LI);
        sb.append(formatNewLine());
    }

//    @Override
//    public void endMetadata(StepWhen metadata) {
//        sb.append(formatCurrentIndent());
//        sb.append(formatNewLine());
//    }


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
    public void visitMetadata(BinaryMetadata metadata) {
        sb.append(formatCurrentIndent());
        sb.append(formatOperator(metadata));
        sb.append(formatNewLine());
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
        sb.append("<div class='VALIDATIONRULE' id='PLACEHOLDER'>");
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


    //    Binary



    @Override
    protected String formatField(Readable field) {
        return "<span class='FIELD' id='PLACEHOLDER'>" + field.readable() + "</span>";
    }

    @Override
    protected String formatOperator(Readable operator) {
        return "<span class='OPERATOR' id='PLACEHOLDER'>" + operator.readable() + "</span>";
    }

}
