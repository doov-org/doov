package org.modelmap.gen;

import org.modelmap.core.FieldId;
import org.modelmap.core.PathConstraint;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.*;

final class ModelVisitor {

    public static void visitModel(Class<?> clazz, Visitor visitor, String packageFilter)
            throws IntrospectionException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        visitModel(clazz, visitor, new LinkedList<>(), packageFilter, 0);
    }

    private static void visitModel(Class<?> clazz, Visitor visitor, LinkedList<Method> path, String packageFilter,
                                   int deep) throws IntrospectionException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (clazz == null)
            return;
        if (clazz.getPackage() == null)
            return;
        if (!clazz.getPackage().getName().startsWith(packageFilter))
            return;
        if (deep > 8)
            return;
        final BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor desc : propertyDescriptors) {
            path.addLast(desc.getReadMethod());
            try {
                final Map<FieldId, PathConstraint> formParam = getFieldTarget(clazz, desc);
                if (formParam == null || formParam.isEmpty()) {
                    continue;
                }
                visitor.visit(formParam, desc.getReadMethod(), desc.getWriteMethod(), path);
            } finally {
                path.removeLast();
            }
        }
        final Collection<Method> otherMethods = methods(clazz, packageFilter);
        for (Method method : otherMethods) {
            path.addLast(method);
            try {
                visitModel(returnType(method, packageFilter), visitor, path, packageFilter, deep + 1);
            } finally {
                path.removeLast();
            }
        }
        visitModel(clazz.getSuperclass(), visitor, path, packageFilter, deep + 1);
    }

    private static Map<FieldId, PathConstraint> getFieldTarget(Class<?> clazz, PropertyDescriptor desc) {
//        if (desc.getReadMethod() == null || desc.getWriteMethod() == null) {
//            return null;
//        }
//
//        FieldTarget formParam = null;
//        try {
//            Field field = clazz.getDeclaredField(desc.getName());
//            formParam = field.getAnnotation(FieldTarget.class);
//        } catch (NoSuchFieldException e) {
//            // derived field without declared field
//        }
//        if (formParam == null) {
//            formParam = desc.getReadMethod().getAnnotation(FieldTarget.class);
//        }
//        if (formParam == null) {
//            formParam = desc.getWriteMethod().getAnnotation(FieldTarget.class);
//        }
//        return formParam;
        return Collections.emptyMap();
    }

    private static Collection<Method> methods(Class<?> clazz, String packageFilter) {
        if (clazz == null) {
            return Collections.emptySet();
        }
        if (clazz.getPackage() == null) {
            return Collections.emptySet();
        }
        if (!clazz.getPackage().getName().startsWith(packageFilter)) {
            return Collections.emptySet();
        }
        return filter(clazz.getMethods(), packageFilter);
    }

    private static Collection<Method> filter(Method[] methods, String packageFilter) {
        final List<Method> filtered = new ArrayList<>();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            if (Modifier.isNative(method.getModifiers())) {
                continue;
            }
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (method.getReturnType() == null) {
                continue;
            }
            if (method.getReturnType().equals(Void.TYPE)) {
                continue;
            }
            if (method.getParameterTypes().length > 0) {
                continue;
            }
            if (!method.getDeclaringClass().getPackage().getName().startsWith(packageFilter)) {
                continue;
            }
            if (method.getName().toLowerCase().contains("clone")) {
                continue;
            }
            filtered.add(method);
        }
        final List<Method> overrided = new ArrayList<>();
        for (Method method : filtered) {
            for (Method method2 : filtered) {
                if (method == method2) {
                    continue;
                }
                if (!method.getName().equals(method2.getName())) {
                    continue;
                }
                if (method.getReturnType().isAssignableFrom(method2.getReturnType())) {
                    overrided.add(method);
                }
            }
        }
        filtered.removeAll(overrided);
        return filtered;
    }

    private static Class<?> returnType(Method method, String packageFilter) {
        if (method.getGenericReturnType() == null) {
            return method.getReturnType();
        }
        if (method.getGenericReturnType().getTypeName().equals(method.getReturnType().getTypeName())) {
            return method.getReturnType();
        }
        if (method.getGenericReturnType() instanceof Class) {
            return (Class<?>) method.getGenericReturnType();
        }
        if (List.class.isAssignableFrom(method.getReturnType())
                && method.getGenericReturnType() instanceof ParameterizedType) {
            final ParameterizedType returnType = (ParameterizedType) method.getGenericReturnType();
            if (returnType == null) {
                return null;
            }
            if (returnType.getActualTypeArguments() == null) {
                return null;
            }
            if (returnType.getActualTypeArguments().length != 1) {
                return null;
            }
            if (returnType.getActualTypeArguments()[0].toString().startsWith(packageFilter)) {
                return null;
            }
            return (Class<?>) returnType.getActualTypeArguments()[0];
        }
        return method.getReturnType();
    }
}
