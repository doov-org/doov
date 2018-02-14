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

import static io.doov.gen.GeneratorFieldInfo.fromVisitorPath;
import static io.doov.gen.ModelWrapperGen.boxingType;
import static io.doov.gen.ModelWrapperGen.primitiveBoxingType;
import static io.doov.gen.ModelWrapperGen.typeParameters;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

import com.google.common.base.CaseFormat;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.field.DefaultFieldInfo;
import io.doov.core.dsl.field.FieldTypeProvider;
import io.doov.gen.processor.MacroProcessor;
import io.doov.gen.processor.Templates;

final class FieldInfoGen {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    static Map<FieldId, GeneratorFieldInfo> createFieldInfos(Map<FieldId, VisitorPath> fieldPathMap) {
        return fieldPathMap.entrySet().stream().collect(toMap(Map.Entry::getKey,
                f -> fromVisitorPath(f.getValue(), fieldPathMap.values())));
    }

    static String imports(Map<FieldId, GeneratorFieldInfo> fieldPaths, FieldTypeProvider typeProvider) {
        return fieldPaths.entrySet().stream().flatMap(e -> {
            List<String> imports = new ArrayList<>();

            FieldId fieldId = e.getKey();
            GeneratorFieldInfo fieldInfo = e.getValue();
            String boxingType = boxingType(fieldInfo.type(), fieldInfo.genericType(), fieldId.position());

            if (boxingType.contains("<")) {
                Type methodType = fieldInfo.genericType();
                imports.addAll(typeParameters(methodType));
                imports.add(boxingType.substring(0, boxingType.indexOf('<')));
            } else {
                imports.add(boxingType);
            }
            imports.add(fieldId.getClass().getName());
            if (typeProvider != null) {
                Class<? extends FieldInfo> fieldInfoType = typeProvider.fielInfoType(fieldInfo);
                imports.add(fieldInfoType.getName());
            }

            return imports.stream();
        }).filter(clazz -> clazz.contains(".")) // excluding java.lang.* types
                .distinct().sorted()
                .map(clazz -> "import " + clazz + ";")
                .collect(joining("\n"));
    }

    static String methods(Map<FieldId, GeneratorFieldInfo> fieldInfos,
                          FieldTypeProvider typeProvider,
                          boolean enumFieldInfo) {
        return fieldInfos.entrySet().stream().sorted(comparing(e -> e.getKey().name())).map(e -> {
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
            return  MacroProcessor.replaceProperties(Templates.dslFieldMethod, conf);
        }).collect(joining("\n\n"));
    }

    static String constants(Map<FieldId, GeneratorFieldInfo> fieldInfos, boolean enumFieldInfo) {
        return fieldInfos.entrySet().stream().sorted(comparing(e -> e.getKey().name()))
                .map(e -> enumFieldInfo ? writeFieldInfoEnum(e.getValue()) : writeFieldInfo(e.getValue()))
                .collect(joining("\n\n"));
    }

    private static String writeFieldInfo(GeneratorFieldInfo fieldInfo) {
        return "    public static final FieldInfo " + fieldInfo.id().toString()
                + " = " + fieldInfo.writeBuilder() + "\n                    "
                + ".build(ALL);";
    }

    private static String writeFieldInfoEnum(GeneratorFieldInfo fieldInfo) {
        return "    " + fieldInfo.id().toString() + "(" + fieldInfo.writeBuilder() + "\n                    "
                + ".build()),";
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

    private static String fieldInfoType(Class<? extends FieldInfo> infoType, Class<?> type, String rawType,
                                        String genericTypes) {
        if (Enum.class.isAssignableFrom(type)) {
            return infoType.getSimpleName() + "<" + rawType + ">";
        }
        if (Iterable.class.isAssignableFrom(type)) {
            if (genericTypes.isEmpty()) {
                return infoType.getSimpleName() + "<?, " + rawType + ">";
            } else {
                return infoType.getSimpleName() + "<" + genericTypes + ", " + rawType + "<" + genericTypes + ">>";
            }
        }
        if (infoType.getTypeParameters().length != 0) {
            return infoType.getSimpleName() + "<" + rawType +
                    (genericTypes.isEmpty() ? "" : "<" + genericTypes + ">")
                    + ">";
        } else {
            return infoType.getSimpleName();
        }
    }

    private static String formatGenericTypes(Class<?>[] genericTypes) {
        return Arrays.stream(genericTypes).map(Class::getSimpleName).collect(joining(", "));
    }

}
