/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.DefaultOperator.all_match_values;
import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.any_match_values;
import static io.doov.core.dsl.meta.DefaultOperator.not;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.DefaultOperator.validate;
import static io.doov.core.dsl.meta.MappingOperator._else;
import static io.doov.core.dsl.meta.MappingOperator.map;
import static io.doov.core.dsl.meta.MappingOperator.mappings;
import static io.doov.core.dsl.meta.MappingOperator.then;
import static io.doov.core.dsl.meta.MappingOperator.to;
import static io.doov.core.dsl.meta.MappingOperator.using;
import static io.doov.core.dsl.meta.MetadataType.EMPTY;
import static io.doov.core.dsl.meta.MetadataType.FIELD_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.LEAF_VALUE;
import static io.doov.core.dsl.meta.MetadataType.MULTIPLE_MAPPING;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.TEMPLATE_PARAM;
import static io.doov.core.dsl.meta.ReturnType.BOOLEAN;
import static io.doov.core.dsl.meta.ast.HtmlWriter.APOS;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.Element;
import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataType;
import io.doov.core.dsl.meta.Operator;
import io.doov.core.dsl.meta.function.TemplateParamMetadata;
import io.doov.core.dsl.meta.i18n.ResourceBundleProvider;

public class AstMarkdownRenderer {

    private static final List<Operator> AND_OR = asList(and, or);
    private static final List<Operator> MATCH_VALUES = asList(all_match_values, any_match_values);
    private StringBuilder sb;
    private ResourceBundleProvider bundle;
    private Locale locale;

    public AstMarkdownRenderer(StringBuilder sb, ResourceBundleProvider bundle, Locale locale) {
        this.sb = sb;
        this.bundle = bundle;
        this.locale = locale;
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    public void toMarkdown(Metadata metadata) {
        toMarkdown(metadata, new ArrayDeque<>(), 0);
    }

    private void toMarkdown(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        parents.push(metadata);
        try {
            switch (metadata.type()) {
                case RULE:
                    rule(metadata, parents, indent);
                    break;
                case WHEN:
                    when(metadata, parents, indent);
                    break;
                case BINARY_PREDICATE:
                case TEMPLATE_PARAM:
                case MAPPING_LEAF_FLAT_MAP:
                case MAPPING_LEAF_FILTER:
                case MAPPING_LEAF_REDUCE:
                    binary(metadata, parents, indent);
                    break;
                case LEAF_PREDICATE:
                case FIELD_PREDICATE:
                case LEAF_VALUE:
                case MAPPING_LEAF:
                case MAPPING_LEAF_ITERABLE:
                case TEMPLATE_IDENTIFIER:
                case FIELD_PREDICATE_MATCH_ANY:
                    leaf(metadata, parents, indent);
                    break;
                case UNARY_PREDICATE:
                    unary(metadata, parents, indent);
                    break;
                case MAPPING_LEAF_ITERABLE_CONCAT:
                case NARY_PREDICATE:
                    nary(metadata, parents, indent);
                    break;
                case SINGLE_MAPPING:
                    singleMapping(metadata, parents, indent);
                    break;
                case MULTIPLE_MAPPING:
                    multipleMapping(metadata, parents, indent);
                    break;
                case TYPE_CONVERTER:
                    typeConverter(metadata, parents, indent);
                case THEN_MAPPING:
                case ELSE_MAPPING:
                case MAPPING_INPUT:
                    mappingFragment(metadata, parents, indent);
                    break;
                default:
                    throw new IllegalStateException(metadata.type().name());
            }
        } finally {
            parents.pop();
        }
    }

    private void mappingFragment(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        switch (metadata.type()) {
            case THEN_MAPPING:
                sb.append(bundle.get(then, locale));
                sb.append("\n");
                break;
            case ELSE_MAPPING:
                sb.append(bundle.get(_else, locale));
                sb.append("\n");
                break;
            default:
                break;
        }
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        final List<Metadata> childs = metadata.children().collect(toList());
        for (Metadata child : childs) {
            if (childs.indexOf(child) == 0 && pmd.isPresent() && pmd.get().type() != MULTIPLE_MAPPING) {
                toMarkdown(child, parents, indent + 1);
            } else {
                formatIndent(indent + 2);
                sb.append("* ");
                toMarkdown(child, parents, indent + 2);
            }
            if (childs.indexOf(child) != childs.size() - 1)
                sb.append("\n");
        }
    }

    private void typeConverter(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        sb.append(bundle.get(using, locale));
        sb.append(" ");
        leaf(metadata, parents, indent);
    }

    private void singleMapping(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (!pmd.isPresent()) {
            formatIndent(indent);
            sb.append("* ");
        }
        sb.append(bundle.get(map, locale));
        sb.append(" ");
        toMarkdown(metadata.childAt(0), parents, indent);
        sb.append("\n");
        formatIndent(indent + 1);
        sb.append("* ");
        sb.append(bundle.get(to, locale));
        sb.append(" ");
        toMarkdown(metadata.childAt(1), parents, indent + 1);
        sb.append("\n");
    }

    private void multipleMapping(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        if (!pmd.isPresent()) {
            formatIndent(indent);
            sb.append("* ");
        }
        sb.append(bundle.get(mappings, locale));
        sb.append("\n");
        final List<Metadata> childs = metadata.children().filter(m -> m.children().count() > 0).collect(toList());
        for (Metadata child : childs) {
            formatIndent(indent + 1);
            sb.append("* ");
            toMarkdown(child, parents, indent + 1);
            if (childs.indexOf(child) != childs.size() - 1)
                sb.append("\n");
        }
    }

    private void nary(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append("\n");
        final List<Metadata> childs = metadata.children().collect(toList());
        for (Metadata child : childs) {
            formatIndent(indent + 1);
            sb.append("* ");
            toMarkdown(child, parents, indent + 1);
            if (childs.indexOf(child) != childs.size() - 1)
                sb.append("\n");
        }
    }

    private void unary(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        if (metadata.getOperator() == not) {
            sb.append(bundle.get(metadata.getOperator(), locale));
            sb.append("\n");
            formatIndent(indent + 1);
            sb.append("* ");
            toMarkdown(metadata.childAt(0), parents, indent + 1);
        } else {
            toMarkdown(metadata.childAt(0), parents, indent + 1);
            sb.append(" ");
            sb.append(bundle.get(metadata.getOperator(), locale));
        }
    }

    private void leaf(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        final List<Element> elts = new ArrayList<Element>(((LeafMetadata<?>) metadata).elements());
        for (Element e : elts) {
            switch (e.getType()) {
                case OPERATOR:
                    sb.append(bundle.get((Operator) e.getReadable(), locale));
                    break;
                case TEMPORAL_UNIT:
                    sb.append(bundle.get(e.getReadable().readable(), locale));
                    break;
                case FIELD:
                    final Metadata fieldMetadata = ((DslField<?>) e.getReadable()).getMetadata();
                    if (fieldMetadata.type() == TEMPLATE_PARAM) {
                        templateParam((TemplateParamMetadata) fieldMetadata);
                    } else {
                        sb.append(fieldMetadata.readable(locale));
                    }
                    break;
                case STRING_VALUE:
                    sb.append(APOS);
                    sb.append(e.getReadable().readable());
                    sb.append(APOS);
                    break;
                case VALUE:
                    sb.append("'");
                    sb.append(e.getReadable().readable());
                    sb.append("'");
                    break;
                default:
                    sb.append(e.getReadable().readable());
                    break;
            }
            if (elts.indexOf(e) != elts.size() - 1)
                sb.append(" ");
        }
    }

    private void templateParam(TemplateParamMetadata metadata) {
        sb.append("{");
        if (metadata.getRight().type() == EMPTY) {
            sb.append(metadata.childAt(0).readable(locale));
        } else {
            sb.append(metadata.childAt(1).readable(locale));
        }
        sb.append("}");
    }

    private void binary(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        if (metadata.type() == TEMPLATE_PARAM) {
            templateParam((TemplateParamMetadata) metadata);
        } else if (MATCH_VALUES.contains(metadata.getOperator())) {
            toMarkdown(metadata.childAt(0), parents, indent + 1);
            sb.append(" ");
            sb.append(bundle.get(metadata.getOperator(), locale));
            sb.append(" ");
            toMarkdown(metadata.childAt(1), parents, indent + 1);
            sb.append("\n");
        } else if (metadata.getOperator().returnType() == BOOLEAN) {
            final Metadata childAt_0 = metadata.childAt(0);
            final Metadata childAt_1 = metadata.childAt(1);
            if (childAt_0 != null && childAt_1 != null) {
                if (AND_OR.contains(childAt_1.getOperator())) {
                    toMarkdown(metadata.childAt(0), parents, indent + 1);
                    sb.append(" ");
                    sb.append(bundle.get(metadata.getOperator(), locale));
                    sb.append("\n");
                    formatIndent(indent + 1);
                    sb.append("* ");
                    toMarkdown(childAt_1, parents, indent + 1);
                } else if (childAt_1.getOperator().returnType() == BOOLEAN) {
                    toMarkdown(metadata.childAt(0), parents, indent);
                    sb.append(" ");
                    sb.append(bundle.get(metadata.getOperator(), locale));
                    sb.append("\n");
                    formatIndent(indent);
                    sb.append("* ");
                    toMarkdown(childAt_1, parents, indent);
                } else if (childAt_0.type() == FIELD_PREDICATE || (childAt_0.type() != NARY_PREDICATE
                                && (childAt_1.type() == LEAF_VALUE || childAt_1.type() == FIELD_PREDICATE))) {
                    toMarkdown(metadata.childAt(0), parents, indent);
                    sb.append(" ");
                    sb.append(bundle.get(metadata.getOperator(), locale));
                    sb.append(" ");
                    toMarkdown(childAt_1, parents, indent);
                } else {
                    toMarkdown(metadata.childAt(0), parents, indent);
                    sb.append(" ");
                    sb.append(bundle.get(metadata.getOperator(), locale));
                    sb.append("\n");
                    formatIndent(indent);
                    sb.append("* ");
                    toMarkdown(childAt_1, parents, indent);
                }
            }
        } else {
            toMarkdown(metadata.childAt(0), parents, indent + 1);
            sb.append(" ");
            sb.append(bundle.get(metadata.getOperator(), locale));
            sb.append(" ");
            toMarkdown(metadata.childAt(1), parents, indent + 1);
        }
    }

    private void when(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        final Optional<Metadata> pmd = parents.stream().skip(1).findFirst();
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append("\n");
        metadata.children().forEach(md -> {
            formatIndent(indent + 1);
            sb.append("* ");
            toMarkdown(md, parents, indent + 1);
        });
        if (pmd.isPresent() && pmd.get().type() == MetadataType.RULE) {
            sb.append("\n");
            formatIndent(indent);
            sb.append("* ");
            sb.append(bundle.get(validate, locale));
            sb.append("\n");
        }
    }

    private void rule(Metadata metadata, ArrayDeque<Metadata> parents, int indent) {
        formatIndent(indent);
        sb.append("* ");
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append("\n");
        formatIndent(indent + 1);
        sb.append("* ");
        metadata.children().forEach(md -> toMarkdown(md, parents, indent + 1));
    }

    private void formatIndent(int indent) {
        for (int i = 0; i < indent; i++)
            sb.append("  ");
    }
}
