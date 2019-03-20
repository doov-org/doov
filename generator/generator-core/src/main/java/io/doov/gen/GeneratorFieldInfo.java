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

import static com.google.common.base.Strings.isNullOrEmpty;
import static io.doov.core.dsl.field.FieldInfoBuilder.fieldInfo;
import static io.doov.gen.ModelWrapperGen.getterType;
import static io.doov.gen.ModelWrapperGen.primitiveBoxingType;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import io.doov.core.CodeLookup;
import io.doov.core.CodeValuable;
import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.field.DelegatingFieldInfoImpl;

public class GeneratorFieldInfo extends DelegatingFieldInfoImpl {

    private static final Pattern UNDER = Pattern.compile("_");

    private final VisitorPath path;

    public GeneratorFieldInfo(FieldInfo fieldInfo, VisitorPath path) {
        super(fieldInfo);
        this.path = path;
    }

    public VisitorPath getPath() {
        return path;
    }

    static GeneratorFieldInfo fromVisitorPath(VisitorPath path, Collection<VisitorPath> allPaths) {
        return new GeneratorFieldInfo(fieldInfo()
                .fieldId(path.getFieldId())
                .type(getterType(path))
                .readable(formatReadable(path))
                ._transient(path.isTransient())
                .codeValuable(isAssignable(path, CodeValuable.class))
                .codeLookup(isAssignable(path, CodeLookup.class))
                .genericTypes(genericClasses(path))
                .siblings(siblings(path, allPaths).stream().sorted(comparing(FieldId::code)).toArray(FieldId[]::new))
                .build(), path);
    }

    private static boolean isAssignable(VisitorPath path, Class<?> clazz) {
        final Method lastMethod = path.getPath().get(path.getPath().size() - 1);
        return clazz.isAssignableFrom(lastMethod.getReturnType());
    }

    private static Set<FieldId> siblings(VisitorPath currentPath, Collection<VisitorPath> collected) {
        final String currentCanonicalPath = currentPath.canonicalPath();
        return collected.stream().filter(p -> p.getFieldId() != currentPath.getFieldId())
                .filter(p -> p.canonicalPath().equals(currentCanonicalPath))
                .map(VisitorPath::getFieldId)
                .collect(toSet());
    }

    private static String formatReadable(VisitorPath currentPath) {
        return isNullOrEmpty(currentPath.getReadable())
                ? stream(UNDER.split(currentPath.getFieldId().code())).map(String::toLowerCase).collect(joining(" "))
                : currentPath.getReadable();
    }

    private static Class[] genericClasses(VisitorPath path) {
        Type returnType = path.getPath().get(path.getPath().size() - 1).getGenericReturnType();
        Class[] genericClasses = new Class[]{};
        if (returnType instanceof ParameterizedType) {
            ParameterizedType genericReturnType = (ParameterizedType) returnType;
            genericClasses = Arrays.stream(genericReturnType.getActualTypeArguments())
                    .map(t -> (Class<?>) t)
                    .toArray(Class[]::new);
        }
        return genericClasses;
    }

    String writeBuilder() {
        StringBuilder constant = new StringBuilder();
        constant.append("fieldInfo()");
        constant.append("\n                    ");
        constant.append(".fieldId(");
        constant.append(this.id().getClass().getSimpleName());
        constant.append(".");
        constant.append(this.id().toString());
        constant.append(")");
        constant.append("\n                    ");
        constant.append(".readable(\"");
        constant.append(this.readable());
        constant.append("\")");
        constant.append("\n                    ");
        constant.append(".type(");
        if (this.type().isPrimitive()) {
            constant.append(primitiveBoxingType(this.type()));
            constant.append(".TYPE");
        } else {
            constant.append(this.type().getSimpleName());
            constant.append(".class");
        }
        constant.append(")");
        if (this.isTransient()) {
            constant.append("\n                    ");
            constant.append("._transient(");
            constant.append(Boolean.toString(true));
            constant.append(")");
        }
        if (this.isCodeValuable()) {
            constant.append("\n                    ");
            constant.append(".codeValuable(");
            constant.append(Boolean.toString(true));
            constant.append(")");
        }
        if (this.isCodeLookup()) {
            constant.append("\n                    ");
            constant.append(".codeLookup(");
            constant.append(Boolean.toString(true));
            constant.append(")");
        }
        if (this.genericTypes().length != 0) {
            constant.append("\n                    ");
            constant.append(".genericTypes(");
            constant.append(formatGenericTypesClass(this.genericTypes()));
            constant.append(")");
        }
        if (this.siblings().length != 0) {
            constant.append("\n                    ");
            constant.append(".siblings(");
            constant.append(formatSiblings(stream(this.siblings()).collect(toSet())));
            constant.append(")");
        }
        return constant.toString();
    }

    private static String formatSiblings(Set<FieldId> siblings) {
        if (siblings.isEmpty()) {
            return "";
        }
        return siblings.stream()
                .sorted(comparing(FieldId::code))
                .map(f -> f.getClass().getSimpleName() + "." + f.toString())
                .collect(joining(", "));
    }

    private static String formatGenericTypesClass(Class<?>[] genericTypes) {
        return stream(genericTypes).map(c -> c.getSimpleName() + ".class").collect(joining(", "));
    }
}
