/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.gen;

import static com.google.common.base.Strings.isNullOrEmpty;
import static io.doov.gen.ModelWrapperGen.getterBoxingType;
import static io.doov.gen.ModelWrapperGen.getterType;
import static io.doov.gen.ModelWrapperGen.typeParameters;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;

import java.lang.reflect.Type;
import java.text.Normalizer;
import java.time.*;
import java.util.*;
import java.util.regex.Pattern;

import com.google.common.base.CaseFormat;

import io.doov.core.FieldId;
import io.doov.core.dsl.field.*;

final class FieldInfoGen {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern UNDER = Pattern.compile("_");

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
        StringBuilder constants = new StringBuilder();
        StringBuilder methods = new StringBuilder();

        fieldPaths.keySet().stream().sorted(comparing(FieldId::name)).forEach(fieldId -> {
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

            constants.append("    public static final ");
            constants.append(fieldType(type, rawType, genericTypes));
            constants.append(" ");
            constants.append(fieldId.toString());
            constants.append(" = ");
            constants.append(fieldFactoryMethod(type, rawType, genericTypes));
            constants.append("\n                    ");
            constants.append(".fieldId(");
            constants.append(fieldId.getClass().getSimpleName());
            constants.append(".");
            constants.append(fieldId.toString());
            constants.append(")");
            constants.append("\n                    ");
            constants.append(".readable(\"");
            constants.append(formatReadable(fieldId, currentPath));
            constants.append("\")");
            constants.append("\n                    ");
            constants.append(".type(");
            constants.append(rawType);
            constants.append(isPrimitive ? ".TYPE" : ".class");
            constants.append(")");
            if (!genericTypes.isEmpty()) {
                constants.append("\n                    ");
                constants.append(".genericTypes(");
                constants.append(genericTypesAsClass);
                constants.append(")");
            }
            if (!siblings.isEmpty()) {
                constants.append("\n                    ");
                constants.append(".siblings(");
                constants.append(siblings);
                constants.append(")");
            }
            constants.append("\n                    ");
            constants.append(".build(ALL);\n\n");

            methods.append("    public static ");
            methods.append(fieldType(type, rawType, genericTypes));
            methods.append(" ");
            methods.append(formatMethod(fieldId, currentPath));
            methods.append("() {");
            methods.append("\n        ");
            methods.append("return ");
            methods.append(fieldId.toString());
            methods.append(";");
            methods.append("\n    }\n\n");
        });

        return constants.toString() + methods.toString();
    }

    private static String formatMethod(FieldId fieldId, VisitorPath currentPath) {
        String readable = formatReadable(fieldId, currentPath);
        String underscore = WHITESPACE.matcher(readable).replaceAll("_");
        String normalized = Normalizer.normalize(underscore, Normalizer.Form.NFD);
        String latin = NONLATIN.matcher(normalized).replaceAll("").toLowerCase(Locale.ENGLISH);
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, latin);
    }

    private static String formatReadable(FieldId fieldId, VisitorPath currentPath) {
        return isNullOrEmpty(currentPath.getReadable())
                        ? stream(UNDER.split(fieldId.name())).map(String::toLowerCase).collect(joining(" "))
                        : currentPath.getReadable();
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
        if (Iterable.class.isAssignableFrom(type)) {
            if (genericTypes.isEmpty())
                return IterableFieldInfo.class.getSimpleName() + "<?, " + rawType + ">";
            else
                return IterableFieldInfo.class.getSimpleName() + "<" + genericTypes + ", " + rawType + "<"
                                + genericTypes + ">>";
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
        if (Iterable.class.isAssignableFrom(type)) {
            if (genericTypes.isEmpty())
                return "FieldInfoProvider\n                    .<?, " + rawType + "> iterableField()";
            else
                return "FieldInfoProvider\n                    .<" + genericTypes + ", " + rawType + "<"
                                + genericTypes + ">> iterableField()";
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
