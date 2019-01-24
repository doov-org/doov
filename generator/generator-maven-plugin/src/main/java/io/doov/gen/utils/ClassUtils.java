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
package io.doov.gen.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import io.doov.core.dsl.path.ReadMethodRef;
import io.doov.core.dsl.path.WriteMethodRef;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ClassUtils {

    /**
     *  Default values for primitive types
     */
    private static Object[] primitiveObjects = new Object[] {
                    null, /* Object */
                    0, /* int */
                    false, /* boolean */
                    0.0d, /* double */
                    0L, /* long */
                    0.0f, /* float */
                    '\u0000', /* char */
                    (byte) 0, /* byte */
                    (short) 0, /* short */
    };

    /**
     *
     * @param clazz container class
     * @param methodRef read method reference
     * @param <T> container class type
     * @param <R> return type
     * @return method
     */
    public static <T, R> Method getReferencedMethod(Class<T> clazz, ReadMethodRef<T, R> methodRef) {
        return findReferencedMethod(clazz, methodRef::call);
    }

    /**
     * @param clazz container class
     * @param methodRef write method reference
     * @param <T> container class type
     * @param <A1> set type
     * @return method
     */
    @SuppressWarnings("unchecked")
    public static <T, A1> Method getReferencedMethod(Class<T> clazz, WriteMethodRef<T, A1> methodRef) {
        return findReferencedMethod(clazz, t -> {
            Throwable e;
            int i = 0;
            do {
                try {
                    methodRef.call(t, (A1) primitiveObjects[i]);
                    e = null;
                } catch (ClassCastException | NullPointerException throwable) {
                    e = throwable;
                    i++;
                }
            } while (e != null && i <= primitiveObjects.length);
        });
    }

    /**
     * @param clazz container class
     * @param invoker consumer with call to method reference
     * @param <T> continer class type
     * @return method
     */
    @SuppressWarnings("unchecked")
    private static <T> Method findReferencedMethod(Class<T> clazz, Consumer<T> invoker) {
        AtomicReference<Method> ref = new AtomicReference<>();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            ref.set(method);
            return null;
        });
        try {
            invoker.accept((T) enhancer.create());
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format("Invalid method reference on class [%s]", clazz));
        }

        Method method = ref.get();
        if (method == null) {
            throw new IllegalArgumentException(String.format("Invalid method reference on class [%s]", clazz));
        }

        return method;
    }

    /**
     * @param baseClass root class
     * @param path read method reference list
     * @return collected ordered map of container class to method
     */
    public static LinkedHashMap<Class, Method> transformPathToMethod(Class<?> baseClass,
                    List<ReadMethodRef<?, ?>> path) {
        LinkedHashMap<Class, Method> methodPath = new LinkedHashMap<>();
        for (ReadMethodRef<?, ?> methodReference : path) {
            baseClass = extractMethod(baseClass, methodReference, methodPath);
        }
        return methodPath;
    }

    /**
     * @param clazz container class
     * @param readRef read method reference
     * @param collected collected ordered map of container class to method
     * @return return type class
     */
    private static Class extractMethod(Class clazz, ReadMethodRef<?, ?> readRef,
                    LinkedHashMap<Class, Method> collected) {
        Method method = getReferencedMethod(clazz, readRef);
        collected.put(clazz, method);
        if (ParameterizedType.class.isAssignableFrom(method.getGenericReturnType().getClass())) {
            ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
            return (Class) parameterizedType.getActualTypeArguments()[0];
        } else {
            return method.getReturnType();
        }
    }

}
