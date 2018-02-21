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
import static io.doov.gen.VisitorPath.pathByFieldId;
import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.maven.plugin.logging.Log;

import com.google.common.base.Joiner;

import io.doov.core.*;
import io.doov.gen.processor.MacroProcessor;
import io.doov.gen.processor.Templates;

final class ModelWrapperGen {

    static String mapFieldTypeIfStatement(String template, Map<FieldId, VisitorPath> collected) {
        final StringBuilder buffer = new StringBuilder();
        collected.keySet().stream()
                .map((Object::getClass)).distinct()
                .sorted(comparing(Class::getName))
                .forEach(fieldType -> {
                    final Map<String, String> conf = new HashMap<>();
                    conf.put("field.id.type", fieldType.getName());
                    buffer.append(MacroProcessor.replaceProperties(template, conf));
                });
        return buffer.toString();
    }

    static Map<FieldId, VisitorPath> validatePath(List<VisitorPath> collected, Log log) {
        // ensure all field id names are unique for each generated field info
        Map<FieldId, List<VisitorPath>> pathByFieldId = pathByFieldId(collected);

        List<FieldId> invalidFieldId = pathByFieldId.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (!invalidFieldId.isEmpty()) {
            invalidFieldId.forEach(f -> log.debug(f.code() + " - " + pathByFieldId.get(f)));
            throw new IllegalStateException("some field ids have more than one path : " + invalidFieldId.toString());
        }

        Map<FieldId, VisitorPath> paths = new TreeMap<>(comparing(FieldId::code));
        pathByFieldId.forEach((fieldId, fieldPaths) -> paths.put(fieldId, fieldPaths.iterator().next()));
        return paths;
    }

    static String mapFieldProperties(Map<FieldId, VisitorPath> collected, Class<?> modelClass) {
        final StringBuilder buffer = new StringBuilder();

        collected.forEach((fieldId, visitorPath) -> {
            Map<String, String> conf = new HashMap<>();
            conf.put("field.id.name", fieldId.toString());
            conf.put("field.id.type", fieldId.getClass().getName());
            conf.put("supplier.method", supplierMethod(fieldId, visitorPath, modelClass));
            conf.put("consumer.method", consumerMethod(fieldId, visitorPath, modelClass));
            buffer.append(MacroProcessor.replaceProperties(Templates.propertyIdEnum, conf));
        });

        return buffer.toString();
    }

    private static String supplierMethod(FieldId fieldId, VisitorPath path, Class<?> modelClass) {
        Map<String, String> conf = new HashMap<>();
        conf.put("field.class.name", fieldId.getClass().getName());
        conf.put("field.id.name", fieldId.toString());
        conf.put("field.type", getterBoxingType(path, fieldId.position()));
        conf.put("target.model.class.name", modelClass.getSimpleName());
        conf.put("null.check", nullCheck(path));
        conf.put("getter.path", getterPath(path));
        return MacroProcessor.replaceProperties(Templates.propertyLiteralSupplier, conf);
    }

    private static String consumerMethod(FieldId fieldId, VisitorPath path, Class<?> modelClass) {
        final Map<String, String> conf = new HashMap<>();
        conf.put("field.class.name", fieldId.getClass().getName());
        conf.put("field.id.name", fieldId.toString());
        conf.put("field.type", getterBoxingType(path, fieldId.position()));
        conf.put("target.model.class.name", modelClass.getSimpleName());
        conf.put("lazy.init", lazyInit(path));
        conf.put("setter.path", setterPath(path));
        conf.put("param", setterBoxingChecker(path));
        return MacroProcessor.replaceProperties(Templates.propertyLiteralConsumer, conf);
    }

    static String mapGetter(Map<FieldId, VisitorPath> collected) {
        return fieldTypes(collected).stream().map(fieldType -> {
            Map<FieldId, VisitorPath> paths = filterByFieldType(collected, fieldType);
            Map<String, String> conf = new HashMap<>();
            conf.put("field.id.type", fieldType.getName());
            conf.put("switch.content", getterSwitchContent(paths));
            return MacroProcessor.replaceProperties(Templates.mapGetMethod, conf);
        }).collect(joining("\n\n"));
    }

    static String mapSetter(Map<FieldId, VisitorPath> collected) {
        return fieldTypes(collected).stream().map(fieldType -> {
            final Map<FieldId, VisitorPath> paths = filterByFieldType(collected, fieldType);
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.type", fieldType.getName());
            conf.put("switch.content", setterSwitchContent(paths));
            return MacroProcessor.replaceProperties(Templates.mapSetMethod, conf);
        }).collect(joining("\n\n"));
    }

    private static List<Class<?>> fieldTypes(Map<FieldId, VisitorPath> collected) {
        return collected.keySet().stream()
                .map(Object::getClass).distinct()
                .sorted(comparing(Class::getName))
                .collect(toList());
    }

    private static String nullCheck(VisitorPath path) {
        final StringBuilder buffer = new StringBuilder();
        for (int i = 1; i < path.getPath().size(); i++) {
            final Method lastGetMethod = path.getPath().get(i - 1);
            final List<Method> subPaths = path.getPath().subList(0, i);
            buffer.append(nullCheck(subPaths));
            if (List.class.isAssignableFrom(lastGetMethod.getReturnType())) {
                buffer.append(sizeCheck(subPaths, path.getFieldId()));
            }
        }
        return buffer.toString();
    }

    private static String nullCheck(List<Method> paths) {
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        conf.put("partial.path", VisitorPath.getterPath(paths));
        buffer.append(MacroProcessor.replaceProperties(Templates.nullCheckBlock, conf));
        return buffer.toString();
    }

    private static String sizeCheck(List<Method> paths, FieldId fieldId) {
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        conf.put("partial.path", VisitorPath.getterPath(paths));
        conf.put("size", String.valueOf(fieldId.position()));
        conf.put("index", String.valueOf(fieldId.position() - 1));
        buffer.append(MacroProcessor.replaceProperties(Templates.sizeCheckBlock, conf));
        return buffer.toString();
    }

    private static String lazyInit(VisitorPath path) {
        final StringBuilder buffer = new StringBuilder();
        for (int i = 1; i < path.getPath().size(); i++) {
            final Method lastGetMethod = path.getPath().get(i - 1);
            final List<Method> pathSubList = path.getPath().subList(0, i);
            if (List.class.isAssignableFrom(lastGetMethod.getReturnType())) {
                buffer.append(lazyInitList(pathSubList, path.getFieldId(), lastGetMethod));
            } else {
                buffer.append(lazyInit(pathSubList, path.getFieldId(), lastGetMethod));
            }
        }
        return buffer.toString();
    }

    private static String lazyInit(List<Method> paths, FieldId field, Method lastGetMethod) {
        final Class<?> returnType = lastGetMethod.getReturnType();
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        final String setterName = setterName(lastGetMethod);
        conf.put("partial.path", VisitorPath.getterPath(paths));
        conf.put("partial.path.init", setterPath(paths, setterName, field.position(), false));
        conf.put("param", "new " + returnType.getName() + diamond(returnType) + "()");
        buffer.append(MacroProcessor.replaceProperties(Templates.lazyInitBlock, conf));
        return buffer.toString();
    }

    private static String diamond(Class<?> returnType) {
        if (returnType.getTypeParameters().length > 0)
            return "<>";
        return "";
    }

    private static String lazyInitList(List<Method> paths, FieldId field, Method lastGetMethod) {
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        final String setterName = setterName(lastGetMethod);
        conf.put("list.content.as.null", listContentAsNull(paths, field));
        conf.put("partial.path", VisitorPath.getterPath(paths));
        conf.put("partial.path.init", setterPath(paths, setterName, field.position(), false));
        conf.put("param", "new " + ArrayList.class.getName() + "<>()");
        conf.put("index", Integer.toString(field.position() - 1));
        conf.put("position", Integer.toString(field.position()));
        final ParameterizedType paramType = (ParameterizedType) lastGetMethod.getGenericReturnType();
        final Class<?> paramType0 = (Class<?>) paramType.getActualTypeArguments()[0];
        conf.put("target.type", paramType0.getName());
        buffer.append(MacroProcessor.replaceProperties(Templates.lazyInitListBlock, conf));
        return buffer.toString();
    }

    private static String listContentAsNull(List<Method> paths, FieldId field) {
        final StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < field.position() - 1; i++) {
            final Map<String, String> conf = new HashMap<>();
            conf.put("partial.path", VisitorPath.getterPath(paths));
            conf.put("index", Integer.toString(i));
            conf.put("position", Integer.toString(i + 1));
            buffer.append(MacroProcessor.replaceProperties(Templates.lazyInitListBlockNull, conf));
        }
        return buffer.toString();
    }

    private static List<FieldId> sortFields(Set<FieldId> FieldIds) {
        final List<FieldId> sortedList = new ArrayList<>(FieldIds);
        sortedList.sort(comparing(Object::toString));
        return sortedList;
    }

    private static String setterName(Method getMethod) {
        if (getMethod == null) {
            return null;
        }
        if (getMethod.getName().startsWith("get")) {
            return "set" + getMethod.getName().substring(3);
        }
        if (getMethod.getName().startsWith("is")) {
            return "set" + getMethod.getName().substring(2);
        }
        return null;
    }

    private static Map<FieldId, VisitorPath> filterByFieldType(Map<FieldId, VisitorPath> paths, Class<?> type) {
        return paths.keySet().stream()
                .filter(f -> type.isAssignableFrom(f.getClass()))
                .collect(Collectors.toMap(f -> f, paths::get));
    }

    private static String setterSwitchContent(Map<FieldId, VisitorPath> paths) {
        final StringBuilder buffer = new StringBuilder();
        for (FieldId fieldid : sortFields(paths.keySet())) {
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.name", fieldid.toString());
            buffer.append(MacroProcessor.replaceProperties(Templates.setSwitchBlock, conf));
        }
        return buffer.toString();
    }

    private static String getterSwitchContent(Map<FieldId, VisitorPath> paths) {
        final StringBuilder buffer = new StringBuilder();
        for (FieldId fieldId : sortFields(paths.keySet())) {
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.name", fieldId.toString());
            buffer.append(MacroProcessor.replaceProperties(Templates.getSwitchBlock, conf));
        }
        return buffer.toString();
    }

    private static String setterBoxingChecker(VisitorPath path) {
        final Class<?> type = path.getGetMethod().getReturnType();
        if (Integer.TYPE.equals(type) || Double.TYPE.equals(type) || //
                Float.TYPE.equals(type) || Long.TYPE.equals(type) || Short.TYPE.equals(type)) {
            return "value != null ? value : 0";
        }
        if (Boolean.TYPE.equals(type)) {
            return "value != null ? value : false";
        }
        if (Character.TYPE.equals(type)) {
            return "value != null ? value : '\u0000'";
        }
        final Type genericReturnType = path.getGetMethod().getGenericReturnType();
        final int position = path.getFieldId().position();
        if (List.class.isAssignableFrom(type)) {
            final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
            final Type typeArg = parameterizedType.getActualTypeArguments()[0];
            final String genericTypeName = typeName(typeArg);
            if (position != -1) {
                return "value";
            }
            return "value != null ? value : new java.util.ArrayList<" + genericTypeName + ">()";
        }
        return "value";
    }

    static Class<?> getterType(VisitorPath path) {
        return path.getPath().get(path.getPath().size() - 1).getReturnType();
    }

    static String getterBoxingType(VisitorPath path, int position) {
        final Method lastMethod = path.getPath().get(path.getPath().size() - 1);
        final Type genericReturnType = lastMethod.getGenericReturnType();
        final Class<?> type = lastMethod.getReturnType();
        return boxingType(type, genericReturnType, position);
    }

    static String primitiveBoxingType(Class<?> type) {
        if (Integer.TYPE.equals(type)) {
            return Integer.class.getSimpleName();
        }
        if (Double.TYPE.equals(type)) {
            return Double.class.getSimpleName();
        }
        if (Boolean.TYPE.equals(type)) {
            return Boolean.class.getSimpleName();
        }
        if (Float.TYPE.equals(type)) {
            return Float.class.getSimpleName();
        }
        if (Long.TYPE.equals(type)) {
            return Long.class.getSimpleName();
        }
        if (Short.TYPE.equals(type)) {
            return Short.class.getSimpleName();
        }
        if (Character.TYPE.equals(type)) {
            return Character.class.getSimpleName();
        } else {
            return type.getSimpleName();
        }
    }

    static String boxingType(Class<?> type, Type genericReturnType, int position) {
        if (type.isPrimitive()) {
            return primitiveBoxingType(type);
        }
        if ("java.lang".equals(type.getPackage().getName())) {
            return type.getSimpleName();
        }
        if (List.class.isAssignableFrom(type)) {
            if (position != -1 && genericReturnType instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                final Type typeArg = parameterizedType.getActualTypeArguments()[0];
                return typeName(typeArg);
            }
            return genericReturnType.toString();
        }
        if (genericReturnType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
            final List<String> typeNames = new ArrayList<>();
            for (Type typeName : parameterizedType.getActualTypeArguments()) {
                typeNames.add(typeName(typeName));
            }
            return typeName(type) + "<" + Joiner.on(", ").join(typeNames) + ">";
        }
        return typeName(type);
    }

    private static String typeName(final Type argumentType) {
        if (argumentType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) argumentType;
            final Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            final List<String> typeParameters = typeParameters(parameterizedType);
            return rawType.getName() + "<" + Joiner.on(",").join(typeParameters) + ">";
        } else if (argumentType instanceof TypeVariable) {
            final TypeVariable<?> typeVariable = (TypeVariable<?>) argumentType;
            return typeVariable.getName();
        } else {
            return ((Class<?>) argumentType).getName();
        }
    }

    static List<String> typeParameters(Type genericReturnType) {
        final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
        final List<String> parameterizedTypeName = new ArrayList<>();
        for (Type paramType : parameterizedType.getActualTypeArguments()) {
            parameterizedTypeName.add(((Class<?>) paramType).getName());
        }
        return parameterizedTypeName;
    }

    private static String setterPath(VisitorPath path) {
        final String setMethodName = path.getSetMethod() != null ? path.getSetMethod().getName() : null;
        return setterPath(path.getPath(), setMethodName, path.getFieldId().position(), true);
    }

    private static String getterPath(VisitorPath path) {
        final int index = path.getFieldId().position();
        final StringBuilder buffer = new StringBuilder();
        for (Method method : path.getPath()) {
            if (List.class.isAssignableFrom(method.getReturnType()) && index >= 0) {
                buffer.append(method.getName());
                buffer.append("().get(");
                buffer.append(index - 1);
                buffer.append(")");
            } else {
                buffer.append(method.getName());
                buffer.append("()");
            }
            if (path.getPath().indexOf(method) < path.getPath().size() - 1) {
                buffer.append('.');
            }
        }
        return buffer.toString();
    }

    private static String setterPath(List<Method> path, String setMethod, int index, boolean initAtPosition) {
        final StringBuilder buffer = new StringBuilder();
        for (Method method : path) {
            if (List.class.isAssignableFrom(method.getReturnType()) && path.indexOf(method) == path.size() - 1) {
                if (initAtPosition && index != -1) {
                    buffer.append(method.getName());
                    buffer.append("().set(").append(index).append(" ,${param})");
                } else {
                    buffer.append(setMethod);
                    buffer.append("(${param})");
                }
            } else if (List.class.isAssignableFrom(method.getReturnType()) && index >= 0) {
                buffer.append(method.getName());
                buffer.append("().get(");
                buffer.append(index - 1);
                buffer.append(")");
            } else if (!isNullOrEmpty(setMethod) && path.indexOf(method) == path.size() - 1) {
                buffer.append(setMethod);
                buffer.append("(${param})");
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

    static String mapConstructors(String targetClassName,
                    Class<? extends FieldModel> baseClazz,
                    Class<?> modelClass) {
        if (AbstractWrapper.class.equals(baseClazz)) {
            return writeDefaultConstructors(targetClassName, modelClass);
        }
        return Arrays.stream(baseClazz.getDeclaredConstructors())
                        .filter(c -> !Modifier.isPrivate(c.getModifiers()))
                        .map(c -> mapConstructor(targetClassName, c, modelClass))
                        .collect(joining());
    }

    private static String writeDefaultConstructors(String targetClassName, Class<?> modelClass) {
        StringBuilder buffer = new StringBuilder();
        List<String> superCall = asList("fieldInfos()", "new " + modelClass.getSimpleName() + "()");
        buffer.append(writeConstructor(targetClassName, Collections.emptyList(), superCall));
        List<String> parameters = Collections.singletonList(modelClass.getSimpleName() + " model");
        superCall = asList("fieldInfos()", "model");
        buffer.append(writeConstructor(targetClassName, parameters, superCall));
        return buffer.toString();
    }

    private static String mapConstructor(String targetClassName, Constructor<?> constructor, Class<?> modelClass) {
        StringBuilder buffer = new StringBuilder();
        List<String> parameterTypes = new ArrayList<>();
        List<String> superCall = new ArrayList<>();
        Integer modelParameterIndex = null;
        for (int i = 0; i < constructor.getParameters().length; i++) {
            Parameter parameter = constructor.getParameters()[i];
            Type type = parameter.getParameterizedType();
            if (isModelParameter(modelClass, type)) {
                superCall.add("new " + modelClass.getSimpleName() + "()");
                modelParameterIndex = i;
            } else if (isFieldInfoListParameter(type)) {
                superCall.add("fieldInfos()");
            } else {
                parameterTypes.add(parameter.toString());
                superCall.add(parameter.getName());
            }
        }
        buffer.append(writeConstructor(targetClassName, parameterTypes, superCall));
        // if the constructor has a parameter of type model it generates a second constructor keeping it as parameter
        if (modelParameterIndex != null) {
            superCall.set(modelParameterIndex, "model");
            if (parameterTypes.size() > modelParameterIndex) {
                parameterTypes.add(modelParameterIndex, modelClass.getSimpleName() + " model");
            } else {
                parameterTypes.add(modelClass.getSimpleName() + " model");
            }
            buffer.append(writeConstructor(targetClassName, parameterTypes, superCall));
        }
        return buffer.toString();
    }

    private static boolean isModelParameter(Class<?> modelClass, Type type) {
        return Class.class.isAssignableFrom(type.getClass()) && modelClass.equals(type);
    }

    /**
     * Captures List<FieldInfo>
     */
    private static boolean isFieldInfoListParameter(Type type) {
        if (ParameterizedType.class.isAssignableFrom(type.getClass())) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if (List.class.equals(parameterizedType.getRawType())) {
                if (parameterizedType.getActualTypeArguments().length == 1) {
                    Type parameterType = parameterizedType.getActualTypeArguments()[0];
                    return FieldInfo.class.equals(parameterType);
                }
            }
        }
        return false;
    }

    private static String writeConstructor(String targetClassName,
                    List<String> parameters,
                    List<String> superCall) {
        final Map<String, String> conf = new HashMap<>();
        conf.put("target.class.name", targetClassName);
        conf.put("constructor.parameters", parameters.stream().collect(joining(", ")));
        conf.put("constructor.super.call", superCall.stream().collect(joining(", ")));
        return MacroProcessor.replaceProperties(Templates.wrapperConstructor, conf);
    }
}
