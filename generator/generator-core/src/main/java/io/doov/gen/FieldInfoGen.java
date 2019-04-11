/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.gen;

import static io.doov.gen.GeneratorFieldInfo.fromVisitorPath;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.*;
import java.util.stream.Stream;

import io.doov.core.FieldId;

final class FieldInfoGen {

    static Map<FieldId, GeneratorFieldInfo> createFieldInfos(Map<FieldId, VisitorPath> fieldPathMap) {
        return fieldPathMap.entrySet().stream().collect(toMap(Map.Entry::getKey,
                f -> fromVisitorPath(f.getValue(), fieldPathMap.values())));
    }

    static String imports(Map<FieldId, GeneratorFieldInfo> fieldPaths) {
        return fieldPaths.entrySet().stream()
                        .flatMap(FieldInfoGen::toImports)
                        .filter(clazz -> clazz.contains(".")) // exclude primitive types
                        .filter(clazz -> !clazz.contains("java.lang.")) // excluding java.lang.* types
                        .distinct().sorted()
                        .map(clazz -> "import " + clazz + ";")
                        .collect(joining("\n"));
    }

    private static Stream<? extends String> toImports(Map.Entry<FieldId, GeneratorFieldInfo> e) {
        List<String> imports = new ArrayList<>();
        FieldId fieldId = e.getKey();
        imports.add(fieldId.getClass().getName());
        GeneratorFieldInfo fieldInfo = e.getValue();
        imports.add(fieldInfo.type().getName());
        imports.addAll(Arrays.stream(fieldInfo.genericTypes()).map(Class::getName).collect(toSet()));
        return imports.stream();
    }

    static String constants(Map<FieldId, GeneratorFieldInfo> fieldInfos, boolean enumFieldInfo) {
        return fieldInfos.entrySet().stream().sorted(comparing(e -> e.getKey().code()))
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

}
