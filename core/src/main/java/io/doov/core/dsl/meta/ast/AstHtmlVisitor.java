/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.ElementType.STRING_VALUE;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.UNARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.WHEN;
import static java.lang.Math.floor;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Locale;

import io.doov.core.dsl.lang.StepWhen;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.i18n.ResourceProvider;

public class AstHtmlVisitor extends AbstractAstVisitor {

    protected static final String CSS_CLASS_VALIDATION_RULE = "dsl-validation-rule";
    protected static final String CSS_CLASS_VALIDATE = "dsl-token-validate";
    protected static final String CSS_CLASS_BINARY = "dsl-token-binary";
    protected static final String CSS_CLASS_UNARY = "dsl-token-unary";
    protected static final String CSS_CLASS_NARY = "dsl-token-nary";
    protected static final String CSS_CLASS_WHEN = "dsl-token-when";

    protected static final String END_DIV = "</div>";

    protected static final String BEG_LI = "<li>";
    protected static final String END_LI = "</li>";

    protected static final String BEG_OL = "<ol>";
    protected static final String END_OL = "</ol>";

    protected static final String BEG_UL = "<ul>";
    protected static final String END_UL = "</ul>";

    protected final OutputStream ops;
    protected final ResourceProvider bundle;
    protected Locale locale;
    protected boolean closeSum = false;
    protected int closeUnaryUL = 0;
    protected int insideNary = 0;
    protected int nbImbriBinary = 0;
    protected boolean rightSideOfBinary = false;
    private boolean closeUn=false;
    private boolean insideSum = false;
    private boolean noExclusionNextLeaf = false;


    private static final MessageFormat format_bar_not_available = new MessageFormat("<div class=''{0}''>"
                    + "<div class=''percentage-value''> n/a</div><div class=''{1}''>"
                    + "<div class=''{2}'' style=''width:0%;''>"
                    + "</div></div></div>", Locale.US);
    private static final MessageFormat format_bar_percentage = new MessageFormat("<div class=''{0}''>"
                    + "<div class=''percentage-value''>{1} %</div>"
                    + "<div class=''{2}''><div class=''{3}'' style=''width:{4}%;''>"
                    + "</div></div></div>", Locale.US);

    private String exclusionBar(PredicateMetadata metadata, ExclusionBar cssClass) {
        final int nbTrue = metadata.trueEvalCount();
        final int nbFalse = metadata.falseEvalCount();
        if (nbTrue == 0 && nbFalse == 0) {
            return format_bar_not_available.format(new Object[] { cssClass.getWrapperClass(), cssClass.getBorderClass(),
                            cssClass.getFillingClass() });
        }
        final Double percentage = floor((nbTrue / ((double) nbTrue + nbFalse)) * 1000) / 10.0;
        return format_bar_percentage.format(new Object[] { cssClass.getWrapperClass(), percentage,
                        cssClass.getBorderClass(), cssClass.getFillingClass(),
                        percentage });
    }

    public String exclusionBar(ValidationRule rule, ExclusionBar cssClass) {
        PredicateMetadata rm = rule.getRootMetadata() instanceof PredicateMetadata ? ((PredicateMetadata) rule
                        .getRootMetadata()) : null;
        return exclusionBar(rm, cssClass);
    }

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
        if (!insideSum) {
            if (noExclusionNextLeaf) {
                noExclusionNextLeaf = false;
            } else {
                write(exclusionBar(leaf, ExclusionBar.SMALL));
            }
        }
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
        }

        if (metadata.getOperator() != and && metadata.getOperator() != or) {
            write(exclusionBar(metadata, ExclusionBar.BIG));
        }
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata, int depth) {
        if (metadata.getOperator() == and || metadata.getOperator() == or) {
            write("<br>");
        }
        htmlFormatSpan(CSS_CLASS_BINARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));

        if (metadata.getRight().type() == UNARY_PREDICATE) {
            write(BEG_UL);
            closeUn=true;
        }
        rightSideOfBinary = true;

        if (metadata.getOperator() != and && metadata.getOperator() != or) {
            noExclusionNextLeaf = true;
        }
    }

    @Override
    public void endMetadata(BinaryMetadata metadata, int depth) {
        if (nbImbriBinary > 0) {
            write(END_UL);
            nbImbriBinary--;
        }
        if (closeSum) {
            write(END_LI);
            closeSum = false;
        }
        rightSideOfBinary = false;
    }

    // nary metadata
    @Override
    public void startMetadata(NaryMetadata metadata, int depth) {
        if (metadata.getOperator() == sum || metadata.getOperator() == min) {
            insideSum = true;
        }

        if (insideNary == 0 && !rightSideOfBinary && (metadata.getOperator() == sum || metadata.getOperator() == count
                        || metadata.getOperator() == min)) {
            write(BEG_LI);
        }
        if (stackPeek() == WHEN || stackPeek() != BINARY_PREDICATE) {
            write(BEG_LI);
        }

        if (metadata.getOperator() != count && metadata.getOperator() != sum && metadata.getOperator() != min) {
            write(exclusionBar(metadata, ExclusionBar.BIG));
        }
        htmlFormatSpan(CSS_CLASS_NARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));

        rightSideOfBinary = false;
        write(BEG_OL);
        insideNary++;
    }

    @Override
    public void endMetadata(NaryMetadata metadata, int depth) {
        write(END_OL);
        insideNary--;
        if (metadata.getOperator() == sum || metadata.getOperator() == min) {
            insideSum = false;
        }
    }

    // unary metadata
    @Override
    public void visitMetadata(UnaryMetadata metadata, int depth) {
        write(BEG_LI);
        htmlFormatSpan(CSS_CLASS_UNARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));

        if (metadata.children().get(0).type() != MetadataType.LEAF_PREDICATE) {
            write(BEG_UL);
            closeUnaryUL++;
        }
    }

    @Override
    protected void endMetadata(UnaryMetadata metadata, int depth) {
        if (closeUn) {
            write(END_UL);
            closeUn=false;
        }
        if (closeUnaryUL > 0) {
            write(END_UL);
            closeUnaryUL--;
        }
        write(END_LI);
    }

    // validation rule
    @Override
    public void startMetadata(ValidationRule metadata, int depth) {
        formatDivStart(CSS_CLASS_VALIDATION_RULE);
    }

    @Override
    public void visitMetadata(ValidationRule metadata, int depth) {
        htmlFormatSpan(CSS_CLASS_VALIDATE, bundle.get(validate, locale));
    }

    @Override
    public void endMetadata(ValidationRule metadata, int depth) {
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
