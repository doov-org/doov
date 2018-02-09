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
import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.Normalizer;
import java.time.*;
import java.util.*;
import java.util.regex.Pattern;

import com.google.common.base.CaseFormat;

import io.doov.core.*;
import io.doov.core.dsl.field.*;

final class FieldInfoGen {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern UNDER = Pattern.compile("_");

    private static boolean isAssignable(VisitorPath path, Class<?> clazz) {
        final Method lastMethod = path.getPath().get(path.getPath().size() - 1);
        return clazz.isAssignableFrom(lastMethod.getReturnType());
    }

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
                .distinct().sorted()
                .map(clazz -> "import " + clazz + ";")
                .collect(joining("\n"));
    }

    static String methods(Map<FieldId, VisitorPath> fieldPaths, Class<?> fieldClass, boolean enumFieldInfo) {
        final List<String> methods = new ArrayList<>();

        fieldPaths.keySet().stream().sorted(comparing(FieldId::name)).forEach(fieldId -> {
            final VisitorPath currentPath = fieldPaths.get(fieldId);
            final Class<?> type = getterType(fieldPaths.get(fieldId));
            final String boxingType = getterBoxingType(fieldPaths.get(fieldId), fieldId.position());
            final String rawType;
            final String genericTypes;

            if (boxingType.contains("<")) {
                rawType = simpleName(boxingType.substring(0, boxingType.indexOf('<')));
                Type methodType = currentPath.getGetMethod().getGenericReturnType();
                genericTypes = typeParameters(methodType).stream()
                        .map(FieldInfoGen::simpleName)
                        .collect(joining(", "));
            } else {
                rawType = simpleName(boxingType);
                genericTypes = "";
            }
            String fieldType = fieldType(type, rawType, genericTypes);
            String method;
            if (enumFieldInfo) {
                method = (fieldType.indexOf(">") > 0 ? "    @SuppressWarnings(\"unchecked\")\n" : "") +
                        "    public static " +
                        fieldType +
                        " " +
                        formatMethod(fieldId, currentPath) +
                        "() {\n" +
                        "        return (" +
                        fieldType +
                        ") " +
                        fieldId.toString() +
                        ".delegate();\n" +
                        "    }";
            } else {
                 method = "    public static " + fieldType + " " + formatMethod(fieldId, currentPath) + "() {\n" +
                        "        return " + fieldId.toString() + ";\n" +
                        "    }";
            }
            methods.add(method);
        });
        return methods.stream().collect(joining("\n\n"));
    }

    static String constants(Map<FieldId, VisitorPath> fieldPaths, Class<?> fieldClass, boolean enumFieldInfo) {
        final List<String> constants = new ArrayList<>();

        fieldPaths.keySet().stream().sorted(comparing(FieldId::name)).forEach(fieldId -> {
            final VisitorPath currentPath = fieldPaths.get(fieldId);
            final Class<?> type = getterType(fieldPaths.get(fieldId));
            final String boxingType = getterBoxingType(fieldPaths.get(fieldId), fieldId.position());
            final String siblings = formatSiblings(siblings(currentPath, fieldPaths.values()));
            final String rawType;
            final String genericTypes;
            final String genericTypesAsClass;
            final boolean isPrimitive = currentPath.getGetMethod().getReturnType().isPrimitive();
            final boolean isCodeValuable = isAssignable(currentPath, CodeValuable.class);
            final boolean isCodeLookup = isAssignable(currentPath, CodeLookup.class);

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

            String fieldInfoConstant;
            if (enumFieldInfo) {
                fieldInfoConstant = writeFieldInfoEnum(fieldId, currentPath, type, siblings,
                        rawType, genericTypes, genericTypesAsClass, isPrimitive, isCodeValuable, isCodeLookup);
            } else {
                fieldInfoConstant = writeFieldInfo(fieldId, currentPath, type, siblings,
                        rawType, genericTypes, genericTypesAsClass, isPrimitive, isCodeValuable, isCodeLookup);
            }
            constants.add(fieldInfoConstant);

        });

        return constants.stream().collect(joining("\n\n"));
    }

    private static String writeFieldInfo(FieldId fieldId,
                                         VisitorPath currentPath,
                                         Class<?> type,
                                         String siblings,
                                         String rawType,
                                         String genericTypes,
                                         String genericTypesAsClass,
                                         boolean isPrimitive,
                                         boolean isCodeValuable,
                                         boolean isCodeLookup) {
        StringBuilder constants = new StringBuilder();
        constants.append("    public static final ");
        constants.append(fieldType(type, rawType, genericTypes));
        constants.append(" ");
        constants.append(fieldId.toString());
        constants.append(" = ");
        constants.append(fieldFactoryMethod(type, rawType, genericTypes));
        constants.append("\n                    ");
        writeFieldInfoBuilder(fieldId, currentPath, siblings, rawType, genericTypes, genericTypesAsClass,
                isPrimitive, isCodeValuable, isCodeLookup, constants);
        constants.append("\n                    ");
        constants.append(".build(ALL);");
        return constants.toString();
    }

    private static String writeFieldInfoEnum(FieldId fieldId,
                                         VisitorPath currentPath,
                                         Class<?> type,
                                         String siblings,
                                         String rawType,
                                         String genericTypes,
                                         String genericTypesAsClass,
                                         boolean isPrimitive,
                                         boolean isCodeValuable,
                                         boolean isCodeLookup) {
        StringBuilder constant = new StringBuilder();
        constant.append("    ");
        constant.append(fieldId.toString());
        constant.append("(");
        constant.append(fieldFactoryMethod(type, rawType, genericTypes));
        constant.append("\n                    ");
        writeFieldInfoBuilder(fieldId, currentPath, siblings, rawType, genericTypes, genericTypesAsClass,
                isPrimitive, isCodeValuable, isCodeLookup, constant);
        constant.append("\n                    ");
        constant.append(".build())");
        constant.append(",");
        return constant.toString();
    }

    private static void writeFieldInfoBuilder(FieldId fieldId,
                                              VisitorPath currentPath,
                                              String siblings,
                                              String rawType,
                                              String genericTypes,
                                              String genericTypesAsClass,
                                              boolean isPrimitive,
                                              boolean isCodeValuable,
                                              boolean isCodeLookup,
                                              StringBuilder constant) {
        constant.append(".fieldId(");
        constant.append(fieldId.getClass().getSimpleName());
        constant.append(".");
        constant.append(fieldId.toString());
        constant.append(")");
        constant.append("\n                    ");
        constant.append(".readable(\"");
        constant.append(formatReadable(fieldId, currentPath));
        constant.append("\")");
        constant.append("\n                    ");
        constant.append(".type(");
        constant.append(rawType);
        constant.append(isPrimitive ? ".TYPE" : ".class");
        constant.append(")");
        if (currentPath.isTransient()) {
            constant.append("\n                    ");
            constant.append("._transient(");
            constant.append(Boolean.toString(currentPath.isTransient()));
            constant.append(")");
        }
        if (isCodeValuable) {
            constant.append("\n                    ");
            constant.append(".codeValuable(");
            constant.append(Boolean.toString(isCodeValuable));
            constant.append(")");
        }
        if (isCodeLookup) {
            constant.append("\n                    ");
            constant.append(".codeLookup(");
            constant.append(Boolean.toString(isCodeLookup));
            constant.append(")");
        }
        if (!genericTypes.isEmpty()) {
            constant.append("\n                    ");
            constant.append(".genericTypes(");
            constant.append(genericTypesAsClass);
            constant.append(")");
        }
        if (!siblings.isEmpty()) {
            constant.append("\n                    ");
            constant.append(".siblings(");
            constant.append(siblings);
            constant.append(")");
        }
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
            if (genericTypes.isEmpty()) {
                return IterableFieldInfo.class.getSimpleName() + "<?, " + rawType + ">";
            } else {
                return IterableFieldInfo.class.getSimpleName() + "<" + genericTypes + ", " + rawType + "<"
                        + genericTypes + ">>";
            }
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
            if (genericTypes.isEmpty()) {
                return "FieldInfoProvider\n                    .<?, " + rawType + "> iterableField()";
            } else {
                return "FieldInfoProvider\n                    .<" + genericTypes + ", " + rawType + "<"
                        + genericTypes + ">> iterableField()";
            }
        }
        return "FieldInfoProvider\n                    .<" + rawType
                + (genericTypes.isEmpty() ? "" : "<" + genericTypes + ">")
                + "> defaultField()";
    }

    private static String formatSiblings(Set<FieldId> siblings) {
        if (siblings.isEmpty()) {
            return "";
        }
        return siblings.stream()
                        .sorted(comparing(FieldId::name))
                        .map(f -> f.getClass().getSimpleName() + "." + f.toString())
                        .collect(joining(", "));
    }

    private static Set<FieldId> siblings(VisitorPath currentPath, Collection<VisitorPath> collected) {
        final String currentCanonicalPath = currentPath.canonicalPath();
        return collected.stream().filter(p -> p.getFieldId() != currentPath.getFieldId())
                        .filter(p -> p.canonicalPath().equals(currentCanonicalPath))
                        .map(VisitorPath::getFieldId)
                        .collect(toSet());
    }


}
