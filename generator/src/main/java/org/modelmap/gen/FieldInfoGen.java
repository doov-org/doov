package org.modelmap.gen;

import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static org.modelmap.gen.ModelWrapperGen.getterBoxingType;
import static org.modelmap.gen.ModelWrapperGen.getterType;
import static org.modelmap.gen.ModelWrapperGen.typeParameters;

import java.lang.reflect.Type;
import java.time.*;
import java.util.*;

import org.modelmap.core.FieldId;
import org.modelmap.core.dsl.field.*;

final class FieldInfoGen {

    static String imports(Map<FieldId, VisitorPath> fieldPaths) {
        return fieldPaths.entrySet().stream().flatMap(e -> {
            List<String> imports = new ArrayList<>();

            FieldId fieldId = e.getKey();
            String boxingType = getterBoxingType(fieldPaths.get(fieldId), fieldId.position());
            VisitorPath currentPath = fieldPaths.get(fieldId);

            if (boxingType.contains("<")) {
                Type methodType = currentPath.getGetMethod().getGenericReturnType();
                imports.addAll(typeParameters(methodType));
                imports.add(boxingType.substring(0, boxingType.indexOf('<')));
            } else {
                imports.add(boxingType);
            }
            imports.add(fieldId.getClass().getName());

            return imports.stream();
        }).filter(clazz -> clazz.contains(".")) // excluding java.lang.* types
                        .distinct().sorted().map(clazz -> "import " + clazz + ";").collect(joining("\n"));
    }

    static String constants(Map<FieldId, VisitorPath> fieldPaths) {
        final StringBuilder builder = new StringBuilder();
        fieldPaths.keySet().stream()
                        .sorted(comparing(FieldId::name))
                        .forEach(fieldId -> {
                            final VisitorPath currentPath = fieldPaths.get(fieldId);
                            final Class<?> type = getterType(fieldPaths.get(fieldId));
                            final String boxingType = getterBoxingType(fieldPaths.get(fieldId), fieldId.position());
                            final String siblings = formatSiblings(siblings(currentPath, fieldPaths.values()));
                            final String rawType;
                            final String genericTypes;
                            final String genericTypesAsClass;
                            final boolean isPrimitive = currentPath.getGetMethod().getReturnType().isPrimitive();

                            if (boxingType.contains("<")) {
                                rawType = simpleName(boxingType.substring(0, boxingType.indexOf('<')));
                                Type methodType = currentPath.getGetMethod().getGenericReturnType();
                                genericTypes = typeParameters(methodType).stream().map(t -> simpleName(t))
                                                .collect(joining(", "));
                                genericTypesAsClass = typeParameters(methodType).stream()
                                                .map(t -> simpleName(t) + ".class")
                                                .collect(joining(", "));
                            } else {
                                rawType = simpleName(boxingType);
                                genericTypesAsClass = "";
                                genericTypes = "";
                            }

                            builder.append("    public static final ");
                            builder.append(fieldType(type, rawType, genericTypes));
                            builder.append(" ");
                            builder.append(fieldId.toString());
                            builder.append(" = ");
                            builder.append(fieldFactoryMethod(type, rawType, genericTypes));
                            builder.append("\n                    ");
                            builder.append(".fieldId(");
                            builder.append(fieldId.getClass().getSimpleName());
                            builder.append(".");
                            builder.append(fieldId.toString());
                            builder.append(")");
                            builder.append("\n                    ");
                            builder.append(".readable(\"");
                            builder.append(stream(fieldId.name().split("_"))
                                            .map(String::toLowerCase)
                                            .collect(joining(" ")));
                            builder.append("\")");
                            builder.append("\n                    ");
                            builder.append(".type(");
                            builder.append(rawType);
                            builder.append(isPrimitive ? ".TYPE" : ".class");
                            builder.append(")");
                            if (!genericTypes.isEmpty()) {
                                builder.append("\n                    ");
                                builder.append(".genericTypes(");
                                builder.append(genericTypesAsClass);
                                builder.append(")");
                            }
                            if (!siblings.isEmpty()) {
                                builder.append("\n                    ");
                                builder.append(".siblings(");
                                builder.append(siblings);
                                builder.append(")");
                            }
                            builder.append("\n                    ");
                            builder.append(".build(ALL);\n\n");
                        });
        return builder.toString();
    }

    private static String simpleName(String className) {
        int lastIndex = className.lastIndexOf('.');
        return lastIndex > 0 ? className.substring(lastIndex + 1, className.length()) : className;
    }

    private static String fieldType(Class<?> type, String rawType, String genericTypes) {
        if (String.class.equals(type)) {
            return StringFieldInfo.class.getSimpleName();
        }
        if (Character.class.equals(type) || Character.TYPE.equals(type)) {
            return CharacterFieldInfo.class.getSimpleName();
        }
        if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
            return IntegerFieldInfo.class.getSimpleName();
        }
        if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
            return BooleanFieldInfo.class.getSimpleName();
        }
        if (Double.class.equals(type) || Double.TYPE.equals(type)) {
            return DoubleFieldInfo.class.getSimpleName();
        }
        if (Float.class.equals(type) || Float.TYPE.equals(type)) {
            return FloatFieldInfo.class.getSimpleName();
        }
        if (Long.class.equals(type) || Long.TYPE.equals(type)) {
            return LongFieldInfo.class.getSimpleName();
        }
        if (LocalDate.class.equals(type)) {
            return LocalDateFieldInfo.class.getSimpleName();
        }
        if (LocalDateTime.class.equals(type)) {
            return LocalDateTimeFieldInfo.class.getSimpleName();
        }
        if (LocalTime.class.equals(type)) {
            return LocalTimeFieldInfo.class.getSimpleName();
        }
        if (Enum.class.isAssignableFrom(type)) {
            return EnumFieldInfo.class.getSimpleName() + "<" + rawType + ">";
        }
        return DefaultFieldInfo.class.getSimpleName() + "<" + rawType
                        + (genericTypes.isEmpty() ? "" : "<" + genericTypes + ">") + ">";
    }

    private static String fieldFactoryMethod(Class<?> type, String rawType, String genericTypes) {
        if (String.class.equals(type)) {
            return "stringField()";
        }
        if (Character.class.equals(type) || Character.TYPE.equals(type)) {
            return "characterField()";
        }
        if (Integer.class.equals(type) || Integer.TYPE.equals(type)) {
            return "integerField()";
        }
        if (Boolean.class.equals(type) || Boolean.TYPE.equals(type)) {
            return "booleanField()";
        }
        if (Double.class.equals(type) || Double.TYPE.equals(type)) {
            return "doubleField()";
        }
        if (Float.class.equals(type) || Float.TYPE.equals(type)) {
            return "floatField()";
        }
        if (Long.class.equals(type) || Long.TYPE.equals(type)) {
            return "longField()";
        }
        if (LocalDate.class.equals(type)) {
            return "localDateField()";
        }
        if (LocalDateTime.class.equals(type)) {
            return "localDateTimeField()";
        }
        if (LocalTime.class.equals(type)) {
            return "localTimeField()";
        }
        if (Enum.class.isAssignableFrom(type)) {
            return "FieldInfoProvider\n                    .<" + rawType + "> enumField()";
        }
        return "FieldInfoProvider\n                    .<" + rawType
                        + (genericTypes.isEmpty() ? "" : "<" + genericTypes + ">")
                        + "> defaultField()";
    }

    private static String formatSiblings(Set<FieldId> siblings) {
        if (siblings.isEmpty()) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(", ");
        final Iterator<FieldId> it = siblings.iterator();
        while (it.hasNext()) {
            final FieldId FieldId = it.next();
            builder.append(FieldId.getClass().getName());
            builder.append(".");
            builder.append(FieldId.toString());
            if (it.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private static Set<FieldId> siblings(VisitorPath currentPath, Collection<VisitorPath> collected) {
        final String currentCanonicalPath = currentPath.displayPath();
        final Set<FieldId> siblings = new HashSet<>();
        for (VisitorPath path : collected) {
            if (path.getFieldId() == currentPath.getFieldId()) {
                continue;
            }
            if (path.displayPath().equals(currentCanonicalPath)) {
                siblings.add(path.getFieldId());
            }
        }
        return siblings;
    }
}
