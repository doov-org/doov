package org.modelmap.gen;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.stream.Collectors.toList;
import static org.modelmap.gen.ModelMapGenMojo.template;
import static org.modelmap.gen.VisitorPath.pathByFieldId;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import org.modelmap.core.FieldId;
import org.modelmap.gen.processor.MacroProcessor;

import com.google.common.base.Joiner;

final class ModelWrapperGen {

    private static final String SUPPRESS_WARN_RAW = "@SuppressWarnings({ \"unchecked\", \"rawtypes\" })";

    static String mapFieldTypeIfStatement(String templateFileName, Map<FieldId, VisitorPath> collected) {
        final StringBuilder buffer = new StringBuilder();
        final String template = template(templateFileName);
        collected.keySet().stream()
                        .map((Object::getClass)).distinct()
                        .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                        .forEach(fieldType -> {
                            final Map<String, String> conf = new HashMap<>();
                            conf.put("field.id.type", fieldType.getName());
                            buffer.append(MacroProcessor.replaceProperties(template, conf));
                        });
        return buffer.toString();
    }

    static Map<FieldId, VisitorPath> validatePath(List<VisitorPath> collected) {
        Map<FieldId, List<VisitorPath>> pathByFieldId = pathByFieldId(collected);

        List<FieldId> invalidFieldId = pathByFieldId.entrySet().stream()
                        .filter(e -> e.getValue().size() > 1)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
        if (!invalidFieldId.isEmpty()) {
            throw new IllegalStateException("some field ids have more than one path : " + invalidFieldId.toString());
        }

        Map<FieldId, VisitorPath> paths = new HashMap<>();
        pathByFieldId.forEach((fieldId, fieldPaths) -> paths.put(fieldId, fieldPaths.iterator().next()));
        return paths;
    }

    static String mapGetter(Map<FieldId, VisitorPath> collected, Class<?> modelClass) {
        final StringBuilder buffer = new StringBuilder();
        final List<Class<?>> fieldTypes = fieldTypes(collected);

        final String getterTemplate = template("MapGetMethod.template");
        fieldTypes.forEach(fieldType -> {
            Map<FieldId, VisitorPath> paths = filterByFieldType(collected, fieldType);
            Map<String, String> conf = new HashMap<>();
            conf.put("field.id.type", fieldType.getName());
            conf.put("switch.content", getterSwitchContent(paths));
            buffer.append(MacroProcessor.replaceProperties(getterTemplate, conf));
        });

        final String fieldSupplierTemplate = template("FieldSupplier.template");
        fieldTypes.forEach(fieldType -> {
            Map<FieldId, VisitorPath> paths = filterByFieldType(collected, fieldType);
            for (FieldId fieldid : sortFields(paths.keySet())) {
                final VisitorPath path = paths.get(fieldid);
                final Map<String, String> conf = new HashMap<>();
                conf.put("field.class.name", fieldid.getClass().getName());
                conf.put("field.id.name", fieldid.toString());
                conf.put("field.type", getterBoxingType(path, fieldid.position()));
                conf.put("target.model.class.name", modelClass.getSimpleName());
                conf.put("null.check", nullCheck(path));
                conf.put("getter.path", getterPath(path));
                buffer.append(MacroProcessor.replaceProperties(fieldSupplierTemplate, conf));
            }
        });
        return buffer.toString();
    }

    static String mapSetter(Map<FieldId, VisitorPath> collected, Class<?> modelClass) {
        final StringBuilder buffer = new StringBuilder();
        final List<Class<?>> fieldTypes = fieldTypes(collected);

        final String setterTemplate = template("MapSetMethod.template");
        fieldTypes.forEach(fieldType -> {
            final Map<FieldId, VisitorPath> paths = filterByFieldType(collected, fieldType);
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.type", fieldType.getName());
            conf.put("switch.content", setterSwitchContent(paths));
            buffer.append(MacroProcessor.replaceProperties(setterTemplate, conf));
        });

        final String fieldConsumerTemplate = template("FieldConsumer.template");
        fieldTypes.forEach(fieldType -> {
            final Map<FieldId, VisitorPath> paths = filterByFieldType(collected, fieldType);
            for (FieldId fieldid : sortFields(paths.keySet())) {
                final Map<String, String> conf = new HashMap<>();
                final VisitorPath path = paths.get(fieldid);
                conf.put("field.class.name", fieldid.getClass().getName());
                conf.put("field.id.name", fieldid.toString());
                conf.put("field.type", getterBoxingType(path, fieldid.position()));
                conf.put("target.model.class.name", modelClass.getSimpleName());
                conf.put("lazy.init", lazyInit(path));
                conf.put("setter.path", setterPath(path, true));
                conf.put("param", setterBoxingChecker(path));
                final boolean suppressWarn = path.containsList() || path.containsGenerics();
                conf.put("suppress.warning", suppressWarn ? "\n    " + SUPPRESS_WARN_RAW : "");
                buffer.append(MacroProcessor.replaceProperties(fieldConsumerTemplate, conf));
            }
        });
        return buffer.toString();
    }

    private static List<Class<?>> fieldTypes(Map<FieldId, VisitorPath> collected) {
        return collected.keySet().stream()
                            .map(Object::getClass).distinct()
                            .sorted((o1, o2) -> o1.getName().compareTo(o2.getName()))
                            .collect(toList());
    }

    private static String nullCheck(VisitorPath path) {
        final StringBuilder buffer = new StringBuilder();
        for (int i = 1; i < path.getPath().size(); i++) {
            buffer.append(nullCheck(path.getPath(), i));
            if (path.getFieldId().position() != -1 && i == (path.getPath().size() - 1)) {
                buffer.append(sizeCheck(path.getPath(), i, path.getFieldId().position()));
            }
        }
        return buffer.toString();
    }

    private static String nullCheck(List<Method> paths, int index) {
        final String lazyInitTemplate = template("NullCheckBlock.template");
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        conf.put("partial.path", VisitorPath.getterPath(paths.subList(0, index)));
        buffer.append(MacroProcessor.replaceProperties(lazyInitTemplate, conf));
        return buffer.toString();
    }

    private static String sizeCheck(List<Method> paths, int index, int position) {
        final String lazyInitTemplate = template("SizeCheckBlock.template");
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        conf.put("partial.path", VisitorPath.getterPath(paths.subList(0, index)));
        conf.put("size", String.valueOf(position));
        buffer.append(MacroProcessor.replaceProperties(lazyInitTemplate, conf));
        return buffer.toString();
    }

    private static String lazyInit(VisitorPath path) {
        final StringBuilder buffer = new StringBuilder();
        for (int i = 1; i < path.getPath().size(); i++) {
            final Method lastGetMethod = path.getPath().get(i - 1);
            if (List.class.isAssignableFrom(lastGetMethod.getReturnType())) {
                buffer.append(lazyInitList(path.getPath(), i, path.getFieldId(), lastGetMethod));
            } else {
                buffer.append(lazyInit(path.getPath(), i, path.getFieldId(), lastGetMethod));
            }
        }
        return buffer.toString();
    }

    private static String lazyInit(List<Method> paths, int index, FieldId field, Method lastGetMethod) {
        final String lazyInitTemplate = template("LazyInitBlock.template");
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        final String setterName = setterName(lastGetMethod);
        conf.put("partial.path", VisitorPath.getterPath(paths.subList(0, index)));
        conf.put("partial.path.init", setterPath(paths.subList(0, index), setterName, field.position(), false));
        conf.put("param", "new " + lastGetMethod.getReturnType().getName() + "()");
        buffer.append(MacroProcessor.replaceProperties(lazyInitTemplate, conf));
        return buffer.toString();
    }

    private static String lazyInitList(List<Method> paths, int index, FieldId field, Method lastGetMethod) {
        final String lazyInitTemplate = template("LazyInitListBlock.template");
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        final String setterName = setterName(lastGetMethod);
        conf.put("list.content.as.null", listContentAsNull(paths.subList(0, index), index, field));
        conf.put("partial.path", VisitorPath.getterPath(paths.subList(0, index)));
        conf.put("partial.path.init", setterPath(paths.subList(0, index), setterName, field.position(), false));
        conf.put("param", "new java.util.ArrayList()");
        conf.put("index", Integer.toString(field.position() - 1));
        conf.put("position", Integer.toString(field.position()));
        final ParameterizedType paramType = (ParameterizedType) lastGetMethod.getGenericReturnType();
        final Class<?> paramType0 = (Class<?>) paramType.getActualTypeArguments()[0];
        conf.put("target.type", paramType0.getName());
        buffer.append(MacroProcessor.replaceProperties(lazyInitTemplate, conf));
        return buffer.toString();
    }

    private static String listContentAsNull(List<Method> paths, int index, FieldId field) {
        final StringBuilder buffer = new StringBuilder();
        final String lazyInitTemplate = template("LazyInitListBlockNull.template");
        for (int i = 0; i < field.position() - 1; i++) {
            final Map<String, String> conf = new HashMap<>();
            conf.put("partial.path", VisitorPath.getterPath(paths.subList(0, index)));
            conf.put("index", Integer.toString(i));
            conf.put("position", Integer.toString(i + 1));
            buffer.append(MacroProcessor.replaceProperties(lazyInitTemplate, conf));
        }
        return buffer.toString();
    }

    private static List<FieldId> sortFields(Set<FieldId> FieldIds) {
        final List<FieldId> sortedList = new ArrayList<>(FieldIds);
        sortedList.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
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
        final String switchContent = template("SetSwitchBlock.template");
        for (FieldId fieldid : sortFields(paths.keySet())) {
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.name", fieldid.toString());
            buffer.append(MacroProcessor.replaceProperties(switchContent, conf));
        }
        return buffer.toString();
    }

    private static String getterSwitchContent(Map<FieldId, VisitorPath> paths) {
        final StringBuilder buffer = new StringBuilder();
        final String switchContent = template("GetSwitchBlock.template");
        for (FieldId fieldId : sortFields(paths.keySet())) {
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.name", fieldId.toString());
            buffer.append(MacroProcessor.replaceProperties(switchContent, conf));
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
            if (position != -1)
                return "value";
            return "value != null ? value : new java.util.ArrayList<" + genericTypeName + ">()";
        }
        return "value";
    }

    static String getterBoxingType(VisitorPath path, int position) {
        final Method lastMethod = path.getPath().get(path.getPath().size() - 1);
        final Type genericReturnType = lastMethod.getGenericReturnType();
        final Class<?> type = lastMethod.getReturnType();
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
        }
        if (List.class.isAssignableFrom(type)) {
            if (position != -1 && genericReturnType instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                final Type typeArg = parameterizedType.getActualTypeArguments()[0];
                return typeName(typeArg);
            }
            return genericReturnType.toString();
        }
        if ("java.lang".equals(type.getPackage().getName())) {
            return type.getSimpleName();
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

    static String typeName(final Type argumentType) {
        if (argumentType instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) argumentType;
            final Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            return rawType.getName() + parameterizedTypeName(parameterizedType);
        } else if (argumentType instanceof TypeVariable) {
            final TypeVariable<?> typeVariable = (TypeVariable<?>) argumentType;
            return typeVariable.getName();
        } else {
            return ((Class<?>) argumentType).getName();
        }
    }

    private static String parameterizedTypeName(Type genericReturnType) {
        final ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
        final List<String> parameterizedTypeName = new ArrayList<>();
        for (Type paramType : parameterizedType.getActualTypeArguments()) {
            parameterizedTypeName.add(((Class<?>) paramType).getName());
        }
        return "<" + Joiner.on(",").join(parameterizedTypeName) + ">";
    }

    static String setterPath(VisitorPath path, boolean initAtPosition) {
        final String setMethodName = path.getSetMethod() != null ? path.getSetMethod().getName() : null;
        return setterPath(path.getPath(), setMethodName, path.getFieldId().position(), initAtPosition);
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
}
