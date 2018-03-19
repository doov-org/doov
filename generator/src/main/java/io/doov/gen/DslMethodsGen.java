/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.gen;

import static io.doov.gen.ModelWrapperGen.primitiveBoxingType;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.google.common.base.CaseFormat;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.field.FieldTypeProvider;
import io.doov.gen.processor.MacroProcessor;
import io.doov.gen.processor.Templates;

final class DslMethodsGen {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    static String imports(Map<FieldId, GeneratorFieldInfo> fieldPaths, FieldTypeProvider typeProvider) {
        return fieldPaths.entrySet().stream()
                        .flatMap(e -> toImports(typeProvider, e))
                        .filter(clazz -> clazz.contains(".")) // exclude primitive types
                        .filter(clazz -> !clazz.contains("java.lang.")) // excluding java.lang.* types
                        .distinct().sorted()
                        .map(clazz -> "import " + clazz + ";")
                        .collect(joining("\n"));
    }

    private static Stream<? extends String> toImports(FieldTypeProvider typeProvider,
                    Map.Entry<FieldId, GeneratorFieldInfo> e) {
        List<String> imports = new ArrayList<>();
        GeneratorFieldInfo fieldInfo = e.getValue();
        Class<? extends FieldInfo> fieldInfoType = typeProvider.fielInfoType(fieldInfo);
        imports.add(fieldInfoType.getName());
        if (fieldInfoType.getTypeParameters().length > 0) {
            imports.add(fieldInfo.type().getName());
            imports.addAll(Arrays.stream(fieldInfo.genericTypes()).map(Class::getName).collect(toSet()));
        }
        return imports.stream();
    }

    static String fields(Map<FieldId, GeneratorFieldInfo> fieldInfos,
                         FieldTypeProvider typeProvider,
                         boolean enumFieldInfo) {
        return fieldInfos.entrySet().stream().sorted(comparing(e -> e.getKey().code())).map(e -> {
            final GeneratorFieldInfo fieldInfo = e.getValue();
            final Class<?> type = fieldInfo.type();
            final String rawType = fieldInfo.type().isPrimitive() ? primitiveBoxingType(type) : type.getSimpleName();
            final String genericTypes = formatGenericTypes(fieldInfo.genericTypes());

            final Class<? extends FieldInfo> fieldInfoClass = typeProvider.fielInfoType(fieldInfo);
            final Map<String, String> conf = new HashMap<>();
            String fieldType = fieldInfoType(fieldInfoClass, type, rawType, genericTypes);
            conf.put("field.type", fieldType);
            conf.put("field.type.class", fieldInfoClass.getSimpleName() + (fieldType.indexOf(">") > 0 ? "<>" : ""));
            conf.put("field.readable", formatMethod(fieldInfo.readable()));
            conf.put("field.info.ref", fieldInfo.id().toString() + (enumFieldInfo ? ".delegate()" : ""));
            return MacroProcessor.replaceProperties(Templates.dslFieldMethod, conf);
        }).collect(joining("\n\n"));
    }

    static String iterableMethods(Map<FieldId, GeneratorFieldInfo> fieldInfos,
                                  FieldTypeProvider typeProvider) {
        return fieldInfos.entrySet().stream()
                .filter(e -> e.getKey().position() > 0)
                .collect(groupingBy(e -> e.getValue().getPath().canonicalPath().replaceAll("[0-9]+", "")))
                .entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getValue)
                .sorted(comparing(l -> l.get(0).getValue().readable()))
                .map(fieldInfoList -> {
                    final GeneratorFieldInfo fieldInfo = fieldInfoList.get(0).getValue();
                    final Class<?> type = fieldInfo.type();
                    final String rawType = type.isPrimitive() ? primitiveBoxingType(type) : type.getSimpleName();
                    final String genericTypes = formatGenericTypes(fieldInfo.genericTypes());
                    final Class<? extends FieldInfo> fieldInfoClass = typeProvider.fielInfoType(fieldInfo);
                    String fieldType = fieldInfoType(fieldInfoClass, type, rawType, genericTypes);
                    String fieldList = fieldInfoList.stream()
                            .sorted(comparing(f -> f.getKey().position()))
                            .map(entry -> formatMethod(entry.getValue().readable()))
                            .collect(joining(", "));
                    final Map<String, String> conf = new HashMap<>();
                    conf.put("field.type", fieldType);
                    conf.put("method.name", formatMethod(fieldInfo.readable()).replaceAll("[0-9]+", ""));
                    conf.put("field.info.refs", fieldList);
                    return MacroProcessor.replaceProperties(Templates.dslFieldIterableMethod, conf);
                }).collect(joining("\n\n"));
    }

    private static String fieldInfoType(Class<? extends FieldInfo> infoType, Class<?> type, String rawType,
                    String genericTypes) {
        if (infoType.getTypeParameters().length == 0) {
            return infoType.getSimpleName();
        }
        if (infoType.getTypeParameters().length == 1) {
            return infoType.getSimpleName() + "<" + rawType +
                            (genericTypes.isEmpty() ? "" : "<" + genericTypes + ">") + ">";
        }
        if (infoType.getTypeParameters().length == 2) {
            if (genericTypes.isEmpty()) {
                return infoType.getSimpleName() + "<?, " + rawType + ">";
            } else {
                return infoType.getSimpleName() + "<" + genericTypes + ", " + rawType + "<" + genericTypes + ">>";
            }
        }
        throw new IllegalStateException("FieldInfo type " + infoType.getName() + "is not compatible with generic " +
                        "types");
    }

    private static String formatGenericTypes(Class<?>[] genericTypes) {
        return Arrays.stream(genericTypes).map(Class::getSimpleName).collect(joining(", "));
    }

    private static String formatMethod(String readable) {
        String slug = readable
                        .replace(" and ", " ")
                        .replace(" the ", " ")
                        .replace(" à ", " ")
                        .replace(" d'", " ")
                        .replace(" a ", " ")
                        .replace(" l'", " ")
                        .replace(" du ", " ")
                        .replace(" au ", " ")
                        .replace(" en ", " ")
                        .replace(" de ", " ")
                        .replace(" un ", " ")
                        .replace(" la ", " ")
                        .replace(" le ", " ")
                        .replace(" une ", " ")
                        .replace(" aux ", " ")
                        .replace(" des ", " ")
                        .replace(" pour ", " ")
                        .replace(" avec ", " ")
                        .replaceAll("( )+", " ");
        String underscore = WHITESPACE.matcher(slug).replaceAll("_");
        String normalized = Normalizer.normalize(underscore, Normalizer.Form.NFD);
        String latin = NONLATIN.matcher(normalized).replaceAll("").toLowerCase(Locale.ENGLISH);
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, latin);
    }
}
