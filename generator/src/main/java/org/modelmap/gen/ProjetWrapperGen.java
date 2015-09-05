package org.modelmap.gen;

import org.apache.commons.lang3.StringUtils;
import org.modelmap.core.FieldId;
import org.modelmap.gen.temp.PropertyParsingException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.join;
import static org.modelmap.gen.ModelMapGenMojo.template;
import static org.modelmap.gen.temp.MacroProcessor.replaceProperties;

final class ProjetWrapperGen {
    static final String SUPPRESS_WARN_RAW = "@SuppressWarnings({ \"unchecked\", \"rawtypes\" })";
    static final String SUPPRESS_WARN_UNCHECKED = "@SuppressWarnings(\"unchecked\")\n";
    static final String MISSING_VALUE = "/** missing value **/";

    static String mapFieldTypeIfStatement(String templateFileName, List<VisitorPath> collected) throws IOException,
            PropertyParsingException {
        final StringBuilder buffer = new StringBuilder();
        final String template = template(templateFileName);
        final Set<Class<?>> fieldTypes = new HashSet<>();
        for (VisitorPath path : collected) {
            fieldTypes.add(path.getFieldId().getClass());
        }
        for (Class<?> fieldType : sortClass(fieldTypes)) {
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.type", fieldType.getName());
            buffer.append(replaceProperties(template, conf, MISSING_VALUE));
        }
        return buffer.toString();
    }

    static String mapGetter(List<VisitorPath> collected) throws IOException, PropertyParsingException {
        final StringBuilder buffer = new StringBuilder();
        final String getterTemplate = template("MapGetMethod.template");
        final Set<Class<?>> fieldTypes = new HashSet<>();
        for (VisitorPath path : collected) {
            fieldTypes.add(path.getFieldId().getClass());
        }
        for (Class<?> fieldType : sortClass(fieldTypes)) {
            final Map<FieldId, List<VisitorPath>> pathGroups = pathGroups(filter(collected, fieldType));
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.type", fieldType.getName());
            conf.put("switch.content", getterSwitchContent(pathGroups));
            buffer.append(replaceProperties(getterTemplate, conf, MISSING_VALUE));
        }
        final String fieldSetTemplate = template("FieldGetMethod.template");
        for (Class<?> fieldType : sortClass(fieldTypes)) {
            final Map<FieldId, List<VisitorPath>> pathGroups = pathGroups(filter(collected, fieldType));
            for (FieldId FieldId : sortFields(pathGroups.keySet())) {
                final Map<String, String> conf = new HashMap<>();
                final List<VisitorPath> pathGroup = pathGroups.get(FieldId);
                conf.put("field.class.name", FieldId.getClass().getName());
                conf.put("field.id.name", FieldId.toString());
                conf.put("field.type", getterBoxingType(pathGroup.get(0), FieldId.position()));
                conf.put("null.check", nullCheck(pathGroup.get(0)));
                conf.put("getter.path", getterPath(pathGroup.get(0)));
                final StringBuilder fixMe = new StringBuilder();
                for (int i = 1; i < pathGroup.size(); i++) {
                    fixMe.append("\n        FIXME ");
                    fixMe.append(getterPath(pathGroup.get(i)));
                    fixMe.append(";");
                }
                conf.put("fix.me", fixMe.toString());
                buffer.append(replaceProperties(fieldSetTemplate, conf, MISSING_VALUE));
            }
        }
        return buffer.toString();
    }

    static String mapSetter(List<VisitorPath> collected) throws IOException, PropertyParsingException {
        final StringBuilder buffer = new StringBuilder();
        final String setterTemplate = template("MapSetMethod.template");
        final Set<Class<?>> fieldTypes = new HashSet<>();
        for (VisitorPath path : collected) {
            fieldTypes.add(path.getFieldId().getClass());
        }
        for (Class<?> fieldType : sortClass(fieldTypes)) {
            final Map<FieldId, List<VisitorPath>> pathGroups = pathGroups(filter(collected, fieldType));
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.type", fieldType.getName());
            conf.put("switch.content", setterSwitchContent(pathGroups));
            buffer.append(replaceProperties(setterTemplate, conf, MISSING_VALUE));
        }
        final String fieldSetTemplate = template("FieldSetMethod.template");
        for (Class<?> fieldType : sortClass(fieldTypes)) {
            final Map<FieldId, List<VisitorPath>> pathGroups = pathGroups(filter(collected, fieldType));
            for (FieldId FieldId : sortFields(pathGroups.keySet())) {
                final Map<String, String> conf = new HashMap<>();
                final List<VisitorPath> pathGroup = pathGroups.get(FieldId);
                final VisitorPath firstGroup = pathGroup.get(0);
                conf.put("field.class.name", FieldId.getClass().getName());
                conf.put("field.id.name", FieldId.toString());
                conf.put("field.type", getterBoxingType(firstGroup, FieldId.position()));
                conf.put("lazy.init", lazyInit(firstGroup));
                conf.put("setter.path", setterPath(firstGroup, true));
                conf.put("param", setterBoxingChecker(firstGroup));
                final boolean suppressWarn = firstGroup.containsList() || firstGroup.containsGenerics();
                conf.put("suppress.warning", suppressWarn ? "\n    " + SUPPRESS_WARN_RAW : "");
                final StringBuilder fixMe = new StringBuilder();
                for (int i = 1; i < pathGroup.size(); i++) {
                    fixMe.append("\n        FIXME ");
                    fixMe.append(setterPath(pathGroup.get(i), true));
                    fixMe.append(";");
                }
                conf.put("fix.me", fixMe.toString());
                buffer.append(replaceProperties(fieldSetTemplate, conf, MISSING_VALUE));
            }
        }
        return buffer.toString();
    }

    static Map<FieldId, List<VisitorPath>> pathGroups(List<VisitorPath> paths) {
        final Map<FieldId, List<VisitorPath>> textPaths = new HashMap<>();
        for (VisitorPath path : paths) {
            if (!textPaths.containsKey(path.getFieldId())) {
                textPaths.put(path.getFieldId(), new ArrayList<>());
            }
            textPaths.get(path.getFieldId()).add(path);
        }
        // remove duplicate paths
        for (FieldId FieldId : textPaths.keySet()) {
            List<VisitorPath> fieldPaths = textPaths.get(FieldId);
            Map<String, VisitorPath> pathMap = new HashMap<>();
            for (VisitorPath visitorPath : fieldPaths) {
                pathMap.put(visitorPath.toString(), visitorPath);
            }
            textPaths.put(FieldId, new ArrayList<>(pathMap.values()));
        }
        // sort paths
        for (List<VisitorPath> mappedPaths : textPaths.values()) {
            mappedPaths.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));
        }
        return textPaths;
    }

    private static String nullCheck(VisitorPath path) throws IOException, PropertyParsingException {
        final StringBuilder buffer = new StringBuilder();
        for (int i = 1; i < path.getPath().size(); i++) {
            final Method lastGetMethod = path.getPath().get(i - 1);
            if (List.class.isAssignableFrom(lastGetMethod.getReturnType())) {
                buffer.append(nullCheck(path.getPath(), i));
            } else {
                buffer.append(nullCheck(path.getPath(), i));
            }
        }
        return buffer.toString();
    }

    private static String nullCheck(List<Method> paths, int index) throws IOException, PropertyParsingException {
        final String lazyInitTemplate = template("NullCheckBlock.template");
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        conf.put("partial.path", VisitorPath.getterPath(paths.subList(0, index), true));
        buffer.append(replaceProperties(lazyInitTemplate, conf, MISSING_VALUE));
        return buffer.toString();
    }

    private static String lazyInit(VisitorPath path) throws IOException, PropertyParsingException {
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

    private static String lazyInit(List<Method> paths, int index, FieldId field, Method lastGetMethod)
            throws IOException, PropertyParsingException {
        final String lazyInitTemplate = template("LazyInitBlock.template");
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        final String setterName = setterName(lastGetMethod);
        conf.put("partial.path", VisitorPath.getterPath(paths.subList(0, index), true));
        conf.put("partial.path.init", setterPath(paths.subList(0, index), setterName, field.position(), false));
        conf.put("param", "new " + lastGetMethod.getReturnType().getName() + "()");
        buffer.append(replaceProperties(lazyInitTemplate, conf, MISSING_VALUE));
        return buffer.toString();
    }

    private static String lazyInitList(List<Method> paths, int index, FieldId field, Method lastGetMethod)
            throws IOException, PropertyParsingException {
        final String lazyInitTemplate = template("LazyInitListBlock.template");
        final StringBuilder buffer = new StringBuilder();
        final Map<String, String> conf = new HashMap<>();
        final String setterName = setterName(lastGetMethod);
        conf.put("list.content.as.null", listContentAsNull(paths.subList(0, index), index, field));
        conf.put("partial.path", VisitorPath.getterPath(paths.subList(0, index), true));
        conf.put("partial.path.init", setterPath(paths.subList(0, index), setterName, field.position(), false));
        conf.put("param", "new java.util.ArrayList()");
        conf.put("index", Integer.toString(field.position() - 1));
        conf.put("position", Integer.toString(field.position()));
        final ParameterizedType paramType = (ParameterizedType) lastGetMethod.getGenericReturnType();
        final Class<?> paramType0 = (Class<?>) paramType.getActualTypeArguments()[0];
        conf.put("target.type", paramType0.getName());
        buffer.append(replaceProperties(lazyInitTemplate, conf, MISSING_VALUE));
        return buffer.toString();
    }

    private static String listContentAsNull(List<Method> paths, int index, FieldId field) throws IOException,
            PropertyParsingException {
        final StringBuilder buffer = new StringBuilder();
        final String lazyInitTemplate = template("LazyInitListBlockNull.template");
        for (int i = 0; i < field.position() - 1; i++) {
            final Map<String, String> conf = new HashMap<>();
            conf.put("partial.path", VisitorPath.getterPath(paths.subList(0, index), true));
            conf.put("index", Integer.toString(i));
            conf.put("position", Integer.toString(i + 1));
            buffer.append(replaceProperties(lazyInitTemplate, conf, MISSING_VALUE));
        }
        return buffer.toString();
    }

    static List<Class<?>> sortClass(Set<Class<?>> classSet) {
        final List<Class<?>> sortedList = new ArrayList<>(classSet);
        Collections.sort(sortedList, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return sortedList;
    }

    private static List<FieldId> sortFields(Set<FieldId> FieldIds) {
        final List<FieldId> sortedList = new ArrayList<>(FieldIds);
        Collections.sort(sortedList, new Comparator<FieldId>() {
            @Override
            public int compare(FieldId o1, FieldId o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
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

    static List<VisitorPath> filter(List<VisitorPath> paths, Class<?> type) {
        final List<VisitorPath> collected = new ArrayList<>();
        for (VisitorPath path : paths) {
            if (!type.isAssignableFrom(path.getFieldId().getClass())) {
                continue;
            }
            collected.add(path);
        }
        return collected;
    }

    private static String setterSwitchContent(Map<FieldId, List<VisitorPath>> pathGroups) throws IOException,
            PropertyParsingException {
        final StringBuilder buffer = new StringBuilder();
        final String switchContent = template("SetSwitchBlock.template");
        for (FieldId FieldId : sortFields(pathGroups.keySet())) {
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.name", FieldId.toString());
            final List<VisitorPath> pathGroup = pathGroups.get(FieldId);
            final String setterBoxingType = getterBoxingType(pathGroup.get(0), FieldId.position());
            final StringBuilder caseContent = new StringBuilder();
            if (setterBoxingType.contains("<")) {
                caseContent.append("                " + SUPPRESS_WARN_UNCHECKED + "\n");
            }
            caseContent.append("                ");
            caseContent.append(setterBoxingType).append(" ");
            caseContent.append(FieldId.toString().toLowerCase());
            caseContent.append(" = (");
            caseContent.append(setterBoxingType);
            caseContent.append(") value;\n");

            caseContent.append("                ");
            caseContent.append("set_${field.id.name}(");
            caseContent.append(FieldId.toString().toLowerCase());
            caseContent.append(");\n");
            for (int i = 1; i < pathGroup.size(); i++) {
                caseContent.append("                FIXME ");
                caseContent.append("set_${field.id.name}(");
                caseContent.append(FieldId.toString().toLowerCase());
                caseContent.append(");\n");
            }
            conf.put("case.content", caseContent.toString());
            buffer.append(replaceProperties(switchContent, conf, MISSING_VALUE));
        }
        return buffer.toString();
    }

    private static String getterSwitchContent(Map<FieldId, List<VisitorPath>> pathGroups) throws IOException,
            PropertyParsingException {
        final StringBuilder buffer = new StringBuilder();
        final String switchContent = template("GetSwitchBlock.template");
        for (FieldId FieldId : sortFields(pathGroups.keySet())) {
            final Map<String, String> conf = new HashMap<>();
            conf.put("field.id.name", FieldId.toString());
            final List<VisitorPath> pathGroup = pathGroups.get(FieldId);
            final StringBuilder caseContent = new StringBuilder();
            caseContent.append("get_${field.id.name}();\n");
            for (int i = 1; i < pathGroup.size(); i++) {
                caseContent.append("                    FIXME return ");
                caseContent.append("get_${field.id.name}();\n");
            }
            conf.put("case.content", caseContent.toString());
            buffer.append(replaceProperties(switchContent, conf, MISSING_VALUE));
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

    static final List<String> PROJET_GENERIC_TYPES = asList("A", "B", "C", "D");

    static String getterBoxingType(VisitorPath path, int position) {
        final Method lastMethod = path.getPath().get(path.getPath().size() - 1);
        final Class<?> type = lastMethod.getReturnType();
        final Type genericReturnType = lastMethod.getGenericReturnType();
        if (Object.class.equals(type)) {
            TypeVariable<?> genericType = (TypeVariable<?>) genericReturnType;
            String genericTypeName = genericType.getName();
            if (PROJET_GENERIC_TYPES.contains(genericTypeName)) {
                final int paramTypeIndex = PROJET_GENERIC_TYPES.indexOf(genericTypeName);
                final Class<?> baseClass = path.getBaseClass();
                final ParameterizedType genericSuperclass = (ParameterizedType) baseClass.getGenericSuperclass();
                final Type argumentType = genericSuperclass.getActualTypeArguments()[paramTypeIndex];
                return typeName(argumentType);
            }
        }
        return getterBoxingType(lastMethod, position);
    }

    private static String getterBoxingType(Method lastMethod, int position) {
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
            return typeName(type) + "<" + StringUtils.join(typeNames, ", ") + ">";
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
        return "<" + join(parameterizedTypeName, ",") + ">";
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
            } else if (isNotEmpty(setMethod) && path.indexOf(method) == path.size() - 1) {
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
