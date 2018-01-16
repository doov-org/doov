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
package io.doov.core;

import java.lang.annotation.*;

/**
 * Annotates an annotation used to annotate a bean property (attribute, getter or setter) with a {@code FieldId}
 *
 * <pre>
 * &#64;Path
 * &#64;Retention(RetentionPolicy.RUNTIME)
 * public &#64;interface ModelPath {
 *
 *     ModelFieldId field();
 *
 *     ModelConstraint constraint() default NONE;
 *
 *     String readable() default "";
 *
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Path {
}
