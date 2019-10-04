/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast;

import static io.doov.core.dsl.meta.DefaultOperator.age_at;
import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.ts.ast.writer.TypeScriptWriter.*;
import static java.util.Arrays.asList;

import java.util.*;

import io.doov.core.dsl.DslField;
import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.function.*;
import io.doov.core.dsl.meta.predicate.FieldMetadata;
import io.doov.ts.ast.writer.TypeScriptWriter;

public class AstTSRenderer {

    private static final List<Operator> AND_OR = asList(and, or);

    private final TypeScriptWriter writer;
    private final boolean prettyPrint;

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
                    iterable(metadata, parents);
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

    private String deSerializeValue(Element elt, Metadata parentMetadata) {
        if (parentMetadata instanceof TemporalBiFunctionMetadata) {
            if (parentMetadata.getOperator() == age_at) {
                // Date
                return "DateUtils.newDate('" + elt.getReadable().readable()+"')";
            }
        }
        return elt.getReadable().readable();
    }

    protected void rule(Metadata metadata, ArrayDeque<Metadata> parents) {
        Metadata when = metadata.lastChild();
        if (parents.peekLast() == metadata) {
            writer.write("const");
            writer.write(SPACE);
            writer.write("rule");
            writer.write(SPACE);
            writer.write(ASSIGN);
            writer.write(SPACE);
        }
        toTS(when, parents);
        writer.write(DOT);
        writer.write(operatorToMethod(DefaultOperator.validate));
        writer.write(LEFT_PARENTHESIS);
        writer.write(RIGHT_PARENTHESIS);
        if (parents.peekLast() == metadata) {
            writer.write(COLUMN);
        }
    }

    protected void when(Metadata metadata, ArrayDeque<Metadata> parents) {
        Metadata dsl = metadata.lastChild();
        if (parents.peekLast() == metadata) {
            writer.write("const");
            writer.write(SPACE);
            writer.write("when");
            writer.write(SPACE);
            writer.write(ASSIGN);
            writer.write(SPACE);
        }
        writer.writeGlobalDOOV();
        writer.write(DOT);
        writer.write(operatorToMethod(DefaultOperator.when));
        writer.write(LEFT_PARENTHESIS);
        toTS(dsl, parents);
        writer.write(RIGHT_PARENTHESIS);
        if (prettyPrint) {
            writer.writeNewLine(parents.size());
        }
        if (parents.peekLast() == metadata) {
            writer.write(COLUMN);
        }
    }

    protected void binary(Metadata metadata, ArrayDeque<Metadata> parents) {
        Metadata left = metadata.left().findFirst().get();
        Metadata right = metadata.right().findFirst().get();
        toTS(left, parents);
        if (metadata.getOperator() == DefaultOperator.with) {
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
            for (Element elt : elts) {
                if (elt.getType() == ElementType.STRING_VALUE) {
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
                        writer.write(deSerializeValue(elt, parentMetadata));
                    } else {
                        writer.write(elt.getReadable().readable());
                    }
                } else {
                    writer.write(elt.getReadable().readable());
                }
            }
        }
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
        // TODO import nary function from other places
        writer.writeGlobalDOOV();
        then_else(metadata, parents);
    }

    protected void mappings(Metadata metadata, ArrayDeque<Metadata> parents) {
        if (parents.peekLast() == metadata) {
            writer.write("const");
            writer.write(SPACE);
            writer.write("mappings");
            writer.write(SPACE);
            writer.write(ASSIGN);
            writer.write(SPACE);
        }
        if (metadata.getOperator() == DefaultOperator.no_operator) {
            ConditionalMappingMetadata conditional = (ConditionalMappingMetadata) metadata;
            toTS(conditional.when(), parents);
            toTS(conditional.then(), parents);
            if (conditional.otherwise() != null && conditional.otherwise().children().count() > 0) {
                toTS(conditional.otherwise(), parents);
            }
        } else if (metadata.getOperator() == MappingOperator.mappings) {
            nary(metadata, parents);
        }
        if (parents.peekLast() == metadata) {
            writer.write(COLUMN);
        }
    }

    protected void then_else(Metadata metadata, ArrayDeque<Metadata> parents) {
        writer.write(DOT);
        writer.write(operatorToMethod(metadata.getOperator()));
        writer.write(LEFT_PARENTHESIS);
        if (metadata.getOperator() != MappingOperator.then && prettyPrint) {
            writer.writeNewLine(parents.size());
        }
        Iterator<Metadata> iterator = metadata.children().iterator();
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
        nary(metadata, parents);
    }

    protected void singleMapping(Metadata metadata, ArrayDeque<Metadata> parents) {
        Metadata left = metadata.left().findFirst().get();
        Metadata right = metadata.right().findFirst().get();
        if (parents.peekLast() == metadata) {
            writer.write("const");
            writer.write(SPACE);
            writer.write("mapping");
            writer.write(SPACE);
            writer.write(ASSIGN);
            writer.write(SPACE);
        }
        writer.writeGlobalDOOV();
        writer.write(DOT);
        writer.write(operatorToMethod(MappingOperator.map));
        writer.write(LEFT_PARENTHESIS);
        toTS(left, parents);
        writer.write(RIGHT_PARENTHESIS);
        writer.write(DOT);
        writer.write(operatorToMethod(MappingOperator.to));
        writer.write(LEFT_PARENTHESIS);
        toTS(right, parents);
        writer.write(RIGHT_PARENTHESIS);
        if (parents.peekLast() == metadata) {
            writer.write(COLUMN);
        }
    }

}
