/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.ast.AbstractAstVisitor.Element.BINARY;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.doov.core.dsl.lang.*;
import io.doov.core.dsl.lang.Readable;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.FieldMetadata.Type;

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
        sb.append(formatFieldClass(metadata, "fieldmeta"));
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
        //        Element element = stackPeek();
        //        if (element == BINARY) {
        //            if (binaryDeep > 0) {
        //                sb.append(formatCurrentIndent());
        //                sb.append(BEG_UL);
        //                sb.append(formatNewLine());
        //            }
        //            binaryDeep++;
        //        }
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
        sb.append("<li type='circle'>");
        sb.append(formatOperatorClass(metadata.getOperator(), "binary"));
        sb.append("</li>");
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
        sb.append(formatOperatorClass(metadata.getOperator(), "nary"));
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

    private String formatFieldClass(FieldMetadata field, String classs) {

        StringBuilder sbb = new StringBuilder();
        field.stream().forEach(el -> {

//            if(el.getType() !=  Type.field  || !sbb.toString().contains(el.getReadable().readable())){
                sbb.append("<span class='" + el.getType() + "'> " + el.getReadable().readable()
                                + " </span>");
//            }

        });

        return sbb.toString();
    }

    private String formatOperatorClass(String operator, String classs) {
        return MessageFormat.format("<span class=''{0}'' id=''{1}''>{2}</span>", classs, "", operator);
    }

    @Override
    protected String formatWhen() {
        return "<span class='when'> When </span>";
    }

    @Override
    protected String formatRule() {
        return "<span class='rule'> Rule </span>";
    }
}
