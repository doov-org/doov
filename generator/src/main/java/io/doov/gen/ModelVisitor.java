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

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.beans.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.maven.plugin.logging.Log;

import io.doov.core.*;

final class ModelVisitor {

    private final Log log;

    public ModelVisitor(Log log) {
        this.log = log;
    }

    public void visitModel(Class<?> clazz, Class<? extends FieldId> fieldClass, Visitor visitor, String packageFilter)
            throws IntrospectionException, IllegalArgumentException {
        log.debug("starting visiting class " + clazz.getName());
        visitModel(clazz, fieldClass, visitor, new LinkedList<>(), packageFilter, 0);
    }

    private void visitModel(Class<?> clazz, Class<? extends FieldId> fieldClass, Visitor visitor, LinkedList<Method> path, String packageFilter, int deep)
            throws IntrospectionException, IllegalArgumentException {
        if (clazz == null) {
            return;
        }
        if (clazz.getPackage() == null) {
            return;
        }
        if (!clazz.getPackage().getName().startsWith(packageFilter)) {
            return;
        }
        if (clazz.isEnum()) {
            return;
        }
        if (deep > 8) {
            return;
        }

        log.debug("class " + clazz.getName());
        final BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor desc : propertyDescriptors) {
            log.debug("property " + desc.getName() + " : "
                            + (desc.getPropertyType() != null ? desc.getPropertyType().getSimpleName() : null)
                            + " from " + clazz.getName());
            path.addLast(desc.getReadMethod());
            try {
                final List<PathAnnotation> fieldTarget = getFieldTarget(clazz, desc, fieldClass);
                if (fieldTarget.isEmpty()) {
                    continue;
                }
                log.debug(fieldTarget.size() + " path(s) found from  " + desc);
                visitor.visit(fieldTarget, desc.getReadMethod(), desc.getWriteMethod(), path);
            } finally {
                path.removeLast();
            }
        }
        final Collection<Method> otherMethods = methods(clazz, packageFilter);
        for (Method method : otherMethods) {
            path.addLast(method);
            try {
                visitModel(returnType(method, packageFilter), fieldClass, visitor, path, packageFilter, deep + 1);
            } finally {
                path.removeLast();
            }
        }
        visitModel(clazz.getSuperclass(), fieldClass, visitor, path, packageFilter, deep + 1);
    }

    private List<PathAnnotation> getFieldTarget(Class<?> clazz, PropertyDescriptor desc, Class<? extends FieldId> fieldClass) {
        if (desc.getReadMethod() == null || desc.getWriteMethod() == null) {
            return emptyList();
        }
        List<PathAnnotation> fieldTarget = emptyList();
        try {
            Field field = clazz.getDeclaredField(desc.getName());
            fieldTarget = getFieldTarget(field, fieldClass);
        } catch (NoSuchFieldException e) {
            // derived field without declared field
        }
        if (fieldTarget.isEmpty()) {
            fieldTarget = getFieldTarget(desc.getReadMethod(), fieldClass);
        }
        if (fieldTarget.isEmpty()) {
            fieldTarget = getFieldTarget(desc.getWriteMethod(), fieldClass);
        }
        return fieldTarget;
    }

    private List<PathAnnotation> getFieldTarget(AccessibleObject executable, Class<? extends FieldId> fieldClass) {
        final Annotation[] annotations = executable.getAnnotations();
        log.debug(annotations.length + " annotations to process from " + executable.toString());

        // retrieve declared path annotations type
        Set<Class<? extends Annotation>> pathAnnotations = stream(annotations)
                .filter(a -> a.annotationType().getAnnotation(Path.class) != null)
                .map(Annotation::annotationType)
                .collect(toSet());

        // add those from repeatable annotations
        stream(annotations).forEach(a -> {
            try {
                Method value = a.annotationType().getMethod("value");
                Class<?> returnType = value.getReturnType();
                if (returnType.isArray() && returnType.getComponentType().isAnnotation()) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Annotation> type = (Class<? extends Annotation>) returnType.getComponentType();
                    pathAnnotations.add(type);
                }
            } catch (NoSuchMethodException e) {
                // no a repeatable annotations, skipping
            }
        });

        log.debug(pathAnnotations.size() + " paths annotations to process from " + executable.toString());
        try {
            return pathAnnotations.stream()
                            .map(a -> asList(executable.getAnnotationsByType(a)))
                            .flatMap(Collection::stream)
                            .map(this::asPathAnnotation)
                            .filter(Objects::nonNull)
                            .filter(p -> fieldClass.isAssignableFrom(p.fieldId.getClass()))
                            .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(executable.toString(), e);
        }
    }

    private PathAnnotation asPathAnnotation(Annotation annotation) {
        try {
            log.debug("process annotation " + annotation.toString());
            Method fieldIdGetter = getMethodByClass(annotation.annotationType(), FieldId.class);
            Method constraintGetter = getMethodByClass(annotation.annotationType(), PathConstraint.class);
            Method readableGetter = getMethodByName(annotation.annotationType(), "readable");
            return new PathAnnotation((FieldId) fieldIdGetter.invoke(annotation),
                    (PathConstraint) constraintGetter.invoke(annotation),
                    readableGetter == null ? null : (String) readableGetter.invoke(annotation));
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    private Method getMethodByClass(Class<?> clazz, Class<?> interfaceType) {
        log.debug("process annotation type " + clazz.getName());
        return stream(clazz.getMethods())
                .filter(f -> {
                    log.debug("process annotation field " + f.toString());
                    return interfaceType.isAssignableFrom(f.getReturnType());
                })
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(clazz + " needs method with " + interfaceType));
    }

    private Method getMethodByName(Class<?> clazz, String name) {
        log.debug("process annotation type " + clazz.getName());
        return stream(clazz.getMethods())
                .filter(f -> {
                    log.debug("process annotation field " + f.toString());
                    return f.getName().equals(name);
                })
                .findFirst()
                .orElse(null);
    }

    private Collection<Method> methods(Class<?> clazz, String packageFilter) {
        if (clazz == null) {
            return emptySet();
        }
        if (clazz.getPackage() == null) {
            return emptySet();
        }
        if (!clazz.getPackage().getName().startsWith(packageFilter)) {
            return emptySet();
        }
        return filter(clazz.getMethods(), packageFilter);
    }

    private Collection<Method> filter(Method[] methods, String packageFilter) {
        final List<Method> filtered = stream(methods)
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .filter(m -> !Modifier.isNative(m.getModifiers()))
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .filter(m -> m.getReturnType() != null)
                .filter(m -> !m.getReturnType().equals(Void.TYPE))
                .filter(m -> m.getParameterTypes().length == 0)
                .filter(m -> m.getDeclaringClass().getPackage().getName().startsWith(packageFilter))
                .filter(m -> !m.getName().toLowerCase().contains("clone"))
                .collect(toList());

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

    private Class<?> returnType(Method method, String packageFilter) {
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

    static class PathAnnotation {

        final FieldId fieldId;
        final PathConstraint constraint;
        final String readable;

        private PathAnnotation(FieldId fieldId, PathConstraint constraint, String readable) {
            this.fieldId = fieldId;
            this.constraint = constraint;
            this.readable = readable;
        }

    }

}
