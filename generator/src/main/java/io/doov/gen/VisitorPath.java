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

import java.lang.reflect.Method;
import java.util.*;

import io.doov.core.FieldId;

final class VisitorPath {

    private final Class<?> baseClass;
    private final List<Method> path;
    private final FieldId fieldId;
    private final String readable;
    private final Method getMethod;
    private final Method setMethod;

    public VisitorPath(Class<?> baseClass, List<Method> getPath, FieldId fieldId, String readable,
            Method getMethod, Method setMethod) {
        this.baseClass = baseClass;
        this.path = new ArrayList<>(getPath);
        this.fieldId = fieldId;
        this.readable = readable;
        this.getMethod = getMethod;
        this.setMethod = setMethod;
    }

    public Class<?> getBaseClass() {
        return baseClass;
    }

    public List<Method> getPath() {
        return path;
    }

    public FieldId getFieldId() {
        return fieldId;
    }

    public String getReadable() {
        return readable;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public boolean containsList() {
        return path.stream().anyMatch(p -> List.class.isAssignableFrom(p.getReturnType()));
    }

    public boolean containsGenerics() {
        return path.stream().anyMatch(p -> p.getGenericReturnType().toString().contains("<"));
    }

    public String displayPath() {
        return getterPath(path, fieldId.position());
    }

    @Override
    public String toString() {
        return baseClass.getSimpleName().toLowerCase() + "." + displayPath() + ":" + fieldId;
    }

    static String getterPath(List<Method> path) {
        return getterPath(path, -1);
    }

    static String getterPath(List<Method> path, int index) {
        final StringBuilder buffer = new StringBuilder();
        for (Method method : path) {
            if (List.class.isAssignableFrom(method.getReturnType()) && index >= 0) {
                buffer.append(method.getName());
                buffer.append("().get(");
                buffer.append(index - 1);
                buffer.append(")");
            } else {
                buffer.append(method.getName());
                buffer.append("()");
            }
            if (path.indexOf(method) < path.size() - 1) {
                buffer.append('.');
            }
        }
        return buffer.toString();
    }

    static Map<FieldId, List<VisitorPath>> pathByFieldId(List<VisitorPath> paths) {
        final Map<FieldId, List<VisitorPath>> textPaths = new HashMap<>();
        paths.forEach(path -> textPaths.computeIfAbsent(path.getFieldId(), f -> new ArrayList<>()).add(path));
        // remove duplicate paths
        textPaths.forEach((fieldId, visitorPaths) -> {
            Map<String, VisitorPath> pathMap = new HashMap<>();
            visitorPaths.forEach(p -> pathMap.put(p.toString(), p));
            textPaths.put(fieldId, new ArrayList<>(pathMap.values()));
        });
        // sort paths
        textPaths.values().forEach(mappedPaths -> mappedPaths.sort((o1, o2) -> o1.toString().compareTo(o2.toString())));
        return textPaths;
    }

}
