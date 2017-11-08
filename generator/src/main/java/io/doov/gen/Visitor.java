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

import java.lang.reflect.Method;
import java.util.List;

import io.doov.core.FieldId;
import io.doov.core.PathConstraint;
import io.doov.gen.ModelVisitor.PathAnnotation;

final class Visitor {

    private final Class<?> baseClass;
    private final List<VisitorPath> collected;

    Visitor(Class<?> baseClass, List<VisitorPath> collected) {
        this.baseClass = baseClass;
        this.collected = collected;
    }

    void visit(List<PathAnnotation> fieldTarget, Method getMethod, Method setMethod, List<Method> paths) {
        fieldTarget.forEach(annotation -> {
            if (!checkFieldTargetConstraint(paths, annotation.fieldId, annotation.constraint)) {
                return;
            }
            final VisitorPath path = new VisitorPath(baseClass, paths, annotation.fieldId, annotation.readable,
                            getMethod, setMethod);
            if (contains(path)) {
                return;
            }
            collected.add(path);
        });
    }

    private static boolean checkFieldTargetConstraint(List<Method> paths, FieldId FieldId, PathConstraint constraint) {
        return isNullOrEmpty(constraint.includePath())
                        || VisitorPath.getterPath(paths, FieldId.position()).contains(constraint.includePath());
    }

    private boolean contains(VisitorPath vPath) {
        final String displayPath = vPath.toString();
        return collected.stream().anyMatch(path -> path.toString().equals(displayPath));
    }

}
