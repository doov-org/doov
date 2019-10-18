/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast;

import static io.doov.core.dsl.meta.DefaultOperator.*;
import static io.doov.core.dsl.meta.MappingOperator.map;
import static io.doov.core.dsl.meta.MappingOperator.mappings;
import static io.doov.ts.ast.writer.TypeScriptWriter.*;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.*;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.function.*;
import io.doov.core.dsl.meta.predicate.FieldMetadata;
import io.doov.ts.ast.writer.ImportSpec;
import io.doov.ts.ast.writer.TypeScriptWriter;

public class AstTSRenderer {

    protected static final List<Operator> AND_OR = asList(and, or);
    protected static final List<Operator> BUILT_IN_NARY = asList(count, sum, min, max,
            map, mappings, match_any, match_all, match_none);

    protected final TypeScriptWriter writer;
    protected final boolean prettyPrint;

    public AstTSRenderer(TypeScriptWriter writer) {
        this(writer, false);
    }

    public AstTSRenderer(TypeScriptWriter writer, boolean prettyPrint) {
        this.writer = writer;
        this.prettyPrint = prettyPrint;
    }

    public void toTS(Metadata metadata) {
        toTS(metadata, new ArrayDeque<>());
    }

    // TODO templates are not supported in doov-ts
    protected void toTS(Metadata metadata, ArrayDeque<Metadata> parents) {
        parents.push(metadata);
        try {
            switch (metadata.type()) {
                case RULE:
                    rule(metadata, parents);
                    break;
                case WHEN:
                    when(metadata, parents);
                    break;
                case BINARY_PREDICATE:
                case TEMPLATE_PARAM:
                    binary(metadata, parents);
                    break;
                case LEAF_PREDICATE:
                case FIELD_PREDICATE:
                case LEAF_VALUE:
                case MAPPING_LEAF:
                case TEMPLATE_IDENTIFIER:
                    leaf(metadata, parents);
                    break;
                case MAPPING_LEAF_ITERABLE:
                    iterableArray(metadata, parents);
                    break;
                case TYPE_CONVERTER:
                    typeConverter(metadata, parents);
                    break;
                case UNARY_PREDICATE:
                    unary(metadata, parents);
                    break;
                case NARY_PREDICATE:
                    nary(metadata, parents);
                    break;
                case MULTIPLE_MAPPING:
                    mappings(metadata, parents);
                    break;
                case THEN_MAPPING:
                case ELSE_MAPPING:
                    then_else(metadata, parents);
                    break;
                case MAPPING_INPUT:
                    mappingInput(metadata, parents);
                    break;
                case FIELD_PREDICATE_MATCH_ANY:
                    fieldMatchAny(metadata, parents);
                    break;
                case SINGLE_MAPPING:
                    singleMapping(metadata, parents);
                    break;
                default:
                    throw new IllegalStateException(metadata.type().name());
            }
        } finally {
            parents.pop();
        }
    }

    protected String operatorToMethod(Operator operator) {
        if (operator == DefaultOperator.length_is) {
            return "length";
        } else if (operator == DefaultOperator.equals) {
            return "eq";
        } else if (operator == DefaultOperator.not_equals) {
            return "notEq";
        } else if (operator == DefaultOperator.temporal_minus) {
            return "minus";
        } else if (operator == DefaultOperator.any_match_values) {
            return "matchAny";
        } else if (operator == DefaultOperator.all_match_values) {
            return "allMatch";
        } else if (operator == DefaultOperator.none_match_values) {
            return "noneMatch";
        } else if (operator == DefaultOperator.always_false) {
            return "alwaysFalse";
        } else if (operator == DefaultOperator.always_true) {
            return "alwaysTrue";
        } else if (operator == MappingOperator._else) {
            return "otherwise";
        } else if (operator == DefaultOperator.is) {
            return "eq";
        }
        return toCamelCase(operator.name());
    }

    protected String descriptionToVariable(String description) {
        return toCamelCase(description);
    }

    protected String toCamelCase(String operatorName) {
        StringBuilder stringBuilder = new StringBuilder();
        boolean afterUnderScoreOrWhitespace = false;
        for (char c : operatorName.toCharArray()) {
            if (c != '_' && c != ' ') {
                if (afterUnderScoreOrWhitespace) {
                    stringBuilder.append(String.valueOf(c).toUpperCase());
                } else {
                    stringBuilder.append(c);
                }
                afterUnderScoreOrWhitespace = false;
            } else {
                afterUnderScoreOrWhitespace = true;
            }
        }
        return stringBuilder.toString();
    }

    protected String deSerializeValue(Element elt, Metadata metadata, Metadata parentMetadata) {
        if (parentMetadata instanceof TemporalBiFunctionMetadata) {
            if (parentMetadata.getOperator() == age_at) {
                // Date
                return "new Date('" + elt.getReadable().readable()+"')";
            }
        }
        if (metadata instanceof StaticMetadata) {
            Class valueClass = ((StaticMetadata) metadata).valueClass();
            if (valueClass.equals(LocalDate.class)) {
                return "new Date('" + elt.getReadable().readable()+"')";
            }
            if (valueClass.isEnum()) {
                return valueClass.getSimpleName() + "." + elt.getReadable().readable();
            }
        }
        // FIXME -> EnumClass.ENUM_LITERAL this should me more generic
        if (metadata instanceof LeafMetadata) {
            if (parentMetadata.getOperator() == as_string
                    || parentMetadata.getOperator() == as_a_number
                    || parentMetadata.getOperator() == as) {
                return elt.getReadable().readable();
            }
            String classPrefix = parentMetadata.left().findFirst()
                    .filter(m -> m.type() == MetadataType.FIELD_PREDICATE)
                    .map(m -> (FieldMetadata<?>) m)
                    .map(f -> ((FieldInfo) f.field()))
                    .filter(f -> f.type().isEnum())
                    .map(f -> f.type().getSimpleName())
                    .orElse(null);
            if (classPrefix != null) {
                return classPrefix + "." + elt.getReadable().readable();
            }
        }
        return elt.getReadable().readable();
    }

    protected void rule(Metadata metadata, ArrayDeque<Metadata> parents) {
        Metadata when = metadata.lastChild();
        toTS(when, parents);
        writer.write(DOT);
        writer.write(operatorToMethod(DefaultOperator.validate));
        writer.write(LEFT_PARENTHESIS);
        writer.write(RIGHT_PARENTHESIS);
    }

    protected void when(Metadata metadata, ArrayDeque<Metadata> parents) {
        Metadata dsl = metadata.lastChild();
        writer.writeGlobalDOOV();
        writer.write(DOT);
        writer.write(operatorToMethod(DefaultOperator.when));
        writer.write(LEFT_PARENTHESIS);
        toTS(dsl, parents);
        writer.write(RIGHT_PARENTHESIS);
        if (prettyPrint) {
            writer.writeNewLine(parents.size());
        }
    }

    protected void binary(Metadata metadata, ArrayDeque<Metadata> parents) {
        Metadata left = metadata.left().findFirst().get();
        Metadata right = metadata.right().findFirst().get();
        Metadata parent = parents.size() > 1 ? new ArrayList<>(parents).get(1) : null;
        toTS(left, parents);
        if (parent != null && parent.type() == MetadataType.MAPPING_INPUT) {
            writer.write(COMMA);
            writer.write(SPACE);
            toTS(right);
        } else if (metadata.getOperator() == DefaultOperator.with) {
            String method;
            if (right instanceof TemporalAdjusterMetadata) {
                TemporalAdjusterMetadata adjusterMetadata = (TemporalAdjusterMetadata) right;
                method = operatorToMethod(adjusterMetadata.getOperator());
            } else {
                method = toCamelCase(right.readable());
            }
            writer.write(DOT);
            writer.write(method);
            writer.write(LEFT_PARENTHESIS);
            writer.write(RIGHT_PARENTHESIS);
        } else if (metadata instanceof TemporalBiFunctionMetadata) {
            String methodModifier = "";
            if (right instanceof TemporalFunctionMetadata) {
                TemporalFunctionMetadata temporalMetadata = (TemporalFunctionMetadata) right;
                methodModifier = temporalMetadata.elementsAsList().stream()
                        .filter(e -> e.getType() == ElementType.TEMPORAL_UNIT)
                        .map(e -> e.getReadable().readable())
                        .findFirst().orElse("");
            }
            writer.write(DOT);
            writer.write(toCamelCase(operatorToMethod(metadata.getOperator()) +" "+ methodModifier));
            writer.write(LEFT_PARENTHESIS);
            toTS(right, parents);
            writer.write(RIGHT_PARENTHESIS);
        } else {
            if (AND_OR.contains(metadata.getOperator())) {
                if (prettyPrint) {
                    writer.writeNewLine(parents.size());
                }
            }
            writer.write(DOT);
            writer.write(operatorToMethod(metadata.getOperator()));
            writer.write(LEFT_PARENTHESIS);
            toTS(right, parents);
            writer.write(RIGHT_PARENTHESIS);
        }
    }

    protected void leaf(Metadata metadata, ArrayDeque<Metadata> parents) {
        MetadataType type = metadata.type();
        LeafMetadata<?> leaf = (LeafMetadata<?>) metadata;
        final List<Element> elts = new ArrayList<>(leaf.elements());
        if (type == MetadataType.FIELD_PREDICATE) {
            FieldMetadata fieldMetadata = (FieldMetadata) metadata;
            writer.writeField(fieldMetadata.field());
        } else {
            if (elts.size() == 1 && elts.get(0).getType() == ElementType.OPERATOR) {
                // This is 'probably' an external function
                writer.write(importRequest((Operator) elts.get(0).getReadable(), metadata, parents));
            } else {
                for (Element elt : elts) {
                    if (elt.getType() == ElementType.OPERATOR) {
                        writer.write(operatorToMethod((Operator) elt.getReadable()));
                    } else if (elt.getType() == ElementType.STRING_VALUE) {
                        writer.writeQuote();
                        writer.write(elt.getReadable().readable());
                        writer.writeQuote();
                    } else if (elt.getType() == ElementType.FIELD) {
                        writer.writeField((DslField<?>) elt.getReadable());
                    } else if (elt.getType() == ElementType.UNKNOWN) {
                        writer.write(elt.getReadable().readable().replace("-function- ", ""));
                    } else if (elt.getType() == ElementType.TEMPORAL_UNIT) {
                        // do not write temporal units they are handled as method modifier in #binary
                    } else if (elt.getType() == ElementType.VALUE) {
                        List<Metadata> parentsList = new ArrayList<>(parents);
                        if (parentsList.size() > 1) {
                            Metadata parentMetadata = parentsList.get(1);
                            // TODO guess the value type from the parent operator;
                            writer.write(deSerializeValue(elt, metadata, parentMetadata));
                        } else {
                            writer.write(elt.getReadable().readable());
                        }
                    } else {
                        writer.write(elt.getReadable().readable());
                    }
                }
            }
        }
    }

    protected String importRequest(Operator operator, Metadata metadata, ArrayDeque<Metadata> parents) {
        if (operator == today) {
            writer.addImport(new ImportSpec("DateFunction", "doov"));
            writer.write("DateFunction");
            writer.write(DOT);
            return operatorToMethod(operator) + LEFT_PARENTHESIS + RIGHT_PARENTHESIS;
        }
        return operatorToMethod(operator);
    }

    protected void mappingInput(Metadata metadata, ArrayDeque<Metadata> parents) {
        Metadata converter = metadata.right().findFirst().get();
        Iterator<Metadata> iterator = metadata.left().iterator();
        while (iterator.hasNext()) {
            toTS(iterator.next(), parents);
            if (iterator.hasNext()) {
                writer.write(COMMA);
                writer.write(SPACE);
            }
        }
        // look for #singleMapping
        writer.write(RIGHT_PARENTHESIS);
        writer.write(DOT);
        writer.write(operatorToMethod(MappingOperator.using));
        writer.write(LEFT_PARENTHESIS);
        toTS(converter, parents);
    }

    protected void typeConverter(Metadata metadata, ArrayDeque<Metadata> parents) {
        if (metadata instanceof LeafMetadata) {
            LeafMetadata leaf = (LeafMetadata) metadata;
            // TODO handle type converter imports
            writer.write(descriptionToVariable(String.valueOf(leaf.elements().getLast())));
        } else {
            writer.write(metadata.readable());
        }
    }

    protected void unary(Metadata metadata, ArrayDeque<Metadata> parents) {
        Metadata child = metadata.lastChild();
        toTS(child, parents);
        writer.write(DOT);
        writer.write(operatorToMethod(metadata.getOperator()));
        writer.write(LEFT_PARENTHESIS);
        writer.write(RIGHT_PARENTHESIS);
    }

    protected void nary(Metadata metadata, ArrayDeque<Metadata> parents) {
        if (BUILT_IN_NARY.contains(metadata.getOperator())) {
            writer.writeGlobalDOOV();
            writer.write(DOT);
            writer.write(operatorToMethod(metadata.getOperator()));
        } else {
            writer.write(importRequest(metadata.getOperator(), metadata, parents));
        }
        nary_elements(metadata, parents);
    }

    protected void mappings(Metadata metadata, ArrayDeque<Metadata> parents) {
        if (metadata.getOperator() == MappingOperator.conditional_mappings) {
            ConditionalMappingMetadata conditional = (ConditionalMappingMetadata) metadata;
            toTS(conditional.when(), parents);
            toTS(conditional.then(), parents);
            if (conditional.otherwise() != null && conditional.otherwise().children().count() > 0) {
                toTS(conditional.otherwise(), parents);
            }
        } else if (metadata.getOperator() == mappings) {
            nary(metadata, parents);
        }
    }

    protected void then_else(Metadata metadata, ArrayDeque<Metadata> parents) {
        writer.write(DOT);
        writer.write(operatorToMethod(metadata.getOperator()));
        nary_elements(metadata, parents);
    }

    protected void nary_elements(Metadata metadata, ArrayDeque<Metadata> parents) {
        writer.write(LEFT_PARENTHESIS);
        List<Metadata> children = metadata.children().collect(toList());
        if (metadata.getOperator() != MappingOperator.then && prettyPrint && children.size() > 2) {
            writer.writeNewLine(parents.size());
        }
        Iterator<Metadata> iterator = children.iterator();
        while(iterator.hasNext()) {
            toTS(iterator.next(), parents);
            if (iterator.hasNext()) {
                writer.write(COMMA);
                if (prettyPrint) {
                    writer.writeNewLine(parents.size());
                } else {
                    writer.write(SPACE);
                }
            }
        }
        writer.write(RIGHT_PARENTHESIS);
    }

    protected void iterableArray(Metadata metadata, ArrayDeque<Metadata> parents) {
        writer.write("[");
        iterable(metadata, parents);
        writer.write("]");
    }

    protected void iterable(Metadata metadata, ArrayDeque<Metadata> parents) {
        Iterator<Metadata> iterator = metadata.children().iterator();
        while(iterator.hasNext()) {
            toTS(iterator.next(), parents);
            if (iterator.hasNext()) {
                writer.write(COMMA);
                writer.write(SPACE);
            }
        }
    }

    protected void fieldMatchAny(Metadata metadata, ArrayDeque<Metadata> parents) {
        iterable(metadata, parents);
    }

    protected void singleMapping(Metadata metadata, ArrayDeque<Metadata> parents) {
        Metadata left = metadata.left().findFirst().get();
        Metadata right = metadata.right().findFirst().get();
        writer.writeGlobalDOOV();
        writer.write(DOT);
        if (left instanceof StaticMetadata && ((StaticMetadata) left).value() == null) {
            writer.write("mapNull");
        } else {
            writer.write(operatorToMethod(map));
            writer.write(LEFT_PARENTHESIS);
            toTS(left, parents);
            writer.write(RIGHT_PARENTHESIS);
            writer.write(DOT);
            writer.write(operatorToMethod(MappingOperator.to));
        }
        writer.write(LEFT_PARENTHESIS);
        toTS(right, parents);
        writer.write(RIGHT_PARENTHESIS);
    }

}
