/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.age_at;
import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.as;
import static io.doov.core.dsl.meta.DefaultOperator.as_a_number;
import static io.doov.core.dsl.meta.DefaultOperator.as_string;
import static io.doov.core.dsl.meta.DefaultOperator.count;
import static io.doov.core.dsl.meta.DefaultOperator.min;
import static io.doov.core.dsl.meta.DefaultOperator.minus;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.DefaultOperator.plus;
import static io.doov.core.dsl.meta.DefaultOperator.sum;
import static io.doov.core.dsl.meta.DefaultOperator.times;
import static io.doov.core.dsl.meta.DefaultOperator.today_minus;
import static io.doov.core.dsl.meta.DefaultOperator.today_plus;
import static io.doov.core.dsl.meta.DefaultOperator.validate;
import static io.doov.core.dsl.meta.DefaultOperator.when;
import static io.doov.core.dsl.meta.ElementType.STRING_VALUE;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.LEAF_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.LEAF_VALUE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.UNARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.WHEN;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static java.lang.Math.floor;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Locale;

import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.NaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;
import io.doov.core.dsl.meta.predicate.UnaryPredicateMetadata;

public class AstHtmlVisitor extends AbstractAstVisitor {

    protected static final String CSS_CLASS_VALIDATION_RULE = "dsl-validation-rule";
    protected static final String CSS_CLASS_VALIDATE = "dsl-token-validate";
    protected static final String CSS_CLASS_BINARY = "dsl-token-binary";
    protected static final String CSS_CLASS_UNARY = "dsl-token-unary";
    protected static final String CSS_CLASS_NARY = "dsl-token-nary";
    protected static final String CSS_CLASS_WHEN = "dsl-token-when";

    protected static final String CSS_CLASS_LI_LEAF = "dsl-li-leaf";
    protected static final String CSS_CLASS_LI_BINARY = "dsl-li-binary";
    protected static final String CSS_CLASS_LI_UNARY = "dsl-li-unary";
    protected static final String CSS_CLASS_LI_NARY = "dsl-li-nary";

    protected static final String CSS_CLASS_UL_WHEN = "dsl-ul-when";
    protected static final String CSS_CLASS_UL_BINARY = "dsl-ul-binary";
    protected static final String CSS_CLASS_UL_BINARY_CHILD = "dsl-ul-binary-child";
    protected static final String CSS_CLASS_UL_UNARY = "dsl-ul-unary";

    protected static final String CSS_CLASS_OL_NARY = "dsl-ol-nary";

    protected final OutputStream ops;
    protected final ResourceProvider bundle;
    protected Locale locale;
    @Deprecated
    protected boolean closeSum = false;
    @Deprecated
    protected int closeUnaryUL = 0;
    protected int nbImbriBinary = 0;
    @Deprecated
    private boolean closeUn = false;
    @Deprecated
    private boolean noExclusionNextLeaf = false;

    public static String astToHtml(Metadata metadata, Locale locale) {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        new AstHtmlVisitor(ops, BUNDLE, locale).browse(metadata, 0);
        return new String(ops.toByteArray(), UTF_8);
    }

    private boolean rightSideOfBinary(Metadata metadata) {
        return BINARY_PREDICATE == stackPeekType() && stackPeek().childAt(1) == metadata;
    }

    private long nbImbriBinary() {
        return stackSteam().map(Metadata::type).filter(t -> t == BINARY_PREDICATE).count();
    }

    private boolean insideNary() {
        return stackSteam().map(Metadata::type).filter(t -> t == NARY_PREDICATE).findFirst().isPresent();
    }

    private final boolean insideSum() {
        return stackSteam().map(Metadata::getOperator).filter(op -> op == sum || op == min).findFirst().isPresent();
    }

    private static String beginElement(String elementType, String... classes) {
        return "<" + elementType
                + (classes.length > 0 ? " class='" + String.join(" ", classes) + "'" : "")
                + ">";
    }

    private static String beginElementWithStyle(String elementType, String style, String... classes) {
        return "<" + elementType
                + (classes.length > 0 ? " class='" + String.join(" ", classes) + "'" : "")
                + (style != null ? " style='" + style + "'" : "")
                + ">";
    }

    private static String endElement(String elementType) {
        return "</" + elementType + ">";
    }

    private static String beginLi(String... classes) {
        return beginElement("li", classes);
    }

    private static String endLi() {
        return endElement("li");
    }

    private static String beginUl(String... classes) {
        return beginElement("ul", classes);
    }

    private static String endUl() {
        return endElement("ul");
    }

    private static String beginOl(String... classes) {
        return beginElement("ol", classes);
    }

    private static String endOl() {
        return endElement("ol");
    }

    private static String beginDiv(String... classes) {
        return beginElement("div", classes);
    }

    private static String beginDivWithStyle(String style, String... classes) {
        return beginElementWithStyle("div", style, classes);
    }

    private static String endDiv() {
        return endElement("div");
    }

    private String exclusionBar(PredicateMetadata metadata, ExclusionBar cssClass) {
        final int nbTrue = metadata.trueEvalCount();
        final int nbFalse = metadata.falseEvalCount();
        if (nbTrue == 0 && nbFalse == 0) {
            return formatExclusionBar(cssClass);
        }
        final double percentage = floor((nbTrue / ((double) nbTrue + nbFalse)) * 1000) / 10.0;
        return formatExclusionBar(cssClass, percentage);
    }

    private String formatExclusionBar(ExclusionBar cssClass) {
        return beginDiv(cssClass.getWrapperClass()) +
                beginDiv("percentage-value") + " n/a" + endDiv() +
                beginDiv(cssClass.getBorderClass()) +
                beginDivWithStyle("width:0%;", cssClass.getFillingClass()) + endDiv() +
                endDiv() +
                endDiv();
    }

    private String formatExclusionBar(ExclusionBar cssClass, double percentage) {
        return beginDiv(cssClass.getWrapperClass()) +
                beginDiv("percentage-value") + NumberFormat.getInstance(locale).format(percentage) + " %" + endDiv() +
                beginDiv(cssClass.getBorderClass()) +
                beginDivWithStyle("width:" + percentage + "%;", cssClass.getFillingClass()) + endDiv() +
                endDiv() +
                endDiv();
    }

    public String exclusionBar(ValidationRule rule, ExclusionBar cssClass) {
        Metadata conditionMetadata = rule.getStepWhen().stepCondition().metadata();
        if (conditionMetadata instanceof PredicateMetadata) {
            return exclusionBar((PredicateMetadata) conditionMetadata, cssClass);
        } else {
            return "";
        }
    }

    public AstHtmlVisitor(OutputStream ops, ResourceProvider bundle, Locale locale) {
        this.ops = ops;
        this.bundle = bundle;
        this.locale = locale;
    }

    // step when

    @Override
    public void startWhen(Metadata metadata, int depth) {
        htmlFormatSpan(CSS_CLASS_WHEN, formatWhen());
        write(beginUl(CSS_CLASS_UL_WHEN));
    }

    @Override
    public void beforeChildWhen(Metadata metadata, Metadata child, int depth) {
        if (isLeaf(child.type())) {
            write(beginLi(CSS_CLASS_LI_LEAF));
        }
    }

    @Override
    public void afterChildWhen(Metadata metadata, Metadata child, boolean hasNext, int depth) {
        if (isLeaf(child.type())) {
            write(endLi());
        }
    }

    @Override
    public void endWhen(Metadata metadata, int depth) {
        write(endUl());
    }

    // field metadata

    @Override
    public void startLeaf(LeafMetadata<?> leaf, int depth) {
        if (!insideSum()) {
            if (noExclusionNextLeaf) {
                noExclusionNextLeaf = false;
            } else if (leaf.type() == LEAF_PREDICATE) {
                write(exclusionBar((PredicateMetadata) leaf, ExclusionBar.SMALL));
            }
        }
        leaf.elements().stream().forEach(e -> {
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

    // binary metadata
    @Override
    public void startBinary(BinaryPredicateMetadata metadata, int depth) {
        Metadata leftChild = metadata.getLeft();

        if (NARY_PREDICATE == stackPeekType() && (metadata.getOperator() != or && metadata.getOperator() != and)) {
            write(beginLi(CSS_CLASS_LI_BINARY));
            closeSum = true;
        } else if (leftChild.type() != BINARY_PREDICATE && leftChild.type() != NARY_PREDICATE) {
            write(beginLi(CSS_CLASS_LI_BINARY));
        }
        if (rightSideOfBinary(metadata) && leftChild.type() != NARY_PREDICATE) {
            write(beginUl(CSS_CLASS_UL_BINARY));
            nbImbriBinary++;
        }

        if ((metadata.getOperator() != and && metadata.getOperator() != or)
                && stackPeekType() != UNARY_PREDICATE
                && !isFunctionOperator(metadata.getOperator())) {
            write(exclusionBar(metadata, ExclusionBar.BIG));
        }
    }

    @Override
    public void afterChildBinary(BinaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
        if (hasNext) {
            if (metadata.getOperator() == and || metadata.getOperator() == or) {
                write("<br>");
            }
            htmlFormatSpan(CSS_CLASS_BINARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));

            if (metadata.getRight().type() == UNARY_PREDICATE) {
                write(beginUl(CSS_CLASS_UL_BINARY_CHILD));
                closeUn = true;
            }

            if (metadata.getOperator() != and && metadata.getOperator() != or) {
                noExclusionNextLeaf = true;
            }
        }
    }

    @Override
    public void endBinary(BinaryPredicateMetadata metadata, int depth) {
        if (nbImbriBinary > 0) {
            write(endUl());
            nbImbriBinary--;
        }
        if (closeSum) {
            write(endLi());
            closeSum = false;
        }
    }

    // nary metadata
    @Override
    public void startNary(NaryPredicateMetadata metadata, int depth) {
        if (!insideNary() && !rightSideOfBinary(metadata)
                && (metadata.getOperator() == sum || metadata.getOperator() == count
                        || metadata.getOperator() == min)) {
            write(beginLi(CSS_CLASS_LI_NARY));
        }
        if (stackPeekType() == WHEN || stackPeekType() != BINARY_PREDICATE) {
            write(beginLi(CSS_CLASS_LI_NARY));
        }

        if (metadata.getOperator() != count && metadata.getOperator() != sum && metadata.getOperator() != min) {
            write(exclusionBar(metadata, ExclusionBar.BIG));
        }
        htmlFormatSpan(CSS_CLASS_NARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));

        write(beginOl(CSS_CLASS_OL_NARY));
    }

    @Override
    public void beforeChildNary(NaryPredicateMetadata metadata, Metadata child, int depth) {
        if (isLeaf(child.type())) {
            write(beginLi(CSS_CLASS_LI_LEAF));
        }
    }

    @Override
    public void afterChildNary(NaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
        if (isLeaf(child.type())) {
            write(endLi());
        }
    }

    @Override
    public void endNary(NaryPredicateMetadata metadata, int depth) {
        write(endOl());
    }

    // unary metadata
    @Override
    public void startUnary(UnaryPredicateMetadata metadata, int depth) {
        write(beginLi(CSS_CLASS_LI_UNARY));
        MetadataType childType = metadata.childAt(0).type();
        if (!isLeaf(childType)) {
            htmlFormatSpan(CSS_CLASS_UNARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));
        }
        if (!isLeaf(childType)) {
            write(beginUl(CSS_CLASS_UL_UNARY));
            closeUnaryUL++;
        }
    }

    @Override
    public void endUnary(UnaryPredicateMetadata metadata, int depth) {
        MetadataType childType = metadata.childAt(0).type();
        if (isLeaf(childType)) {
            htmlFormatSpan(CSS_CLASS_UNARY, escapeHtml4(bundle.get(metadata.getOperator(), locale)));
        }
        write(exclusionBar(metadata, ExclusionBar.SMALL));
        if (closeUn) {
            write(endUl());
            closeUn = false;
        }
        if (closeUnaryUL > 0) {
            write(endUl());
            closeUnaryUL--;
        }
        write(endLi());
    }

    // validation rule

    @Override
    public void startRule(Metadata metadata, int depth) {
        formatDivStart(CSS_CLASS_VALIDATION_RULE);
    }

    @Override
    public void endRule(Metadata metadata, int depth) {
        htmlFormatSpan(CSS_CLASS_VALIDATE, bundle.get(validate, locale));
        write(endDiv());
    }

    // metadata

    @Override
    public void afterChildDefault(Metadata metadata, Metadata child, boolean hasNext, int depth) {
        write(metadata.readable());
    }

    // implementation
    protected String formatWhen() {
        return bundle.get(when, locale);
    }

    protected void formatLeafOperator(Element e) {
        htmlFormatSpan("dsl-token-operator", escapeHtml4(bundle.get((Operator) e.getReadable(), locale)));
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
            htmlFormatSpan("dsl-token-value", "'" + escapeHtml4(bundle.get(e.getReadable().readable(), locale)) + "'");
        } else {
            htmlFormatSpan("dsl-token-value", escapeHtml4(bundle.get(e.getReadable().readable(), locale)));
        }
    }

    protected void formatLeafUnknown(Element e) {
        htmlFormatSpan("dsl-token-unknown", escapeHtml4(bundle.get(e.getReadable().readable(), locale)));
    }

    protected void htmlFormatSpan(String cssClass, String content) {
        write("<span class=\"" + cssClass + "\">" + content + "</span> ");
    }

    private void formatDivStart(String cssClass) {
        write("<div class=\"" + cssClass + "\">");
    }

    protected void write(String s) {
        try {
            ops.write(s.getBytes(UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    private boolean isLeaf(MetadataType childType) {
        return childType == LEAF_PREDICATE || childType == LEAF_VALUE || childType == FIELD_PREDICATE;
    }

    private boolean isFunctionOperator(Operator operator) {
        return operator == times
                || operator == as_string
                || operator == as
                || operator == as_a_number
                || operator == plus
                || operator == minus
                || operator == today_plus
                || operator == today_minus
                || operator == age_at;
    }
}
