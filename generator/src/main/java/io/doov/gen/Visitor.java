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
        fieldTarget.forEach(pathAnnotation -> {
            if (!checkFieldTargetConstraint(paths, pathAnnotation.fieldId, pathAnnotation.constraint)) {
                return;
            }
            final VisitorPath path = new VisitorPath(baseClass, paths, pathAnnotation.fieldId, pathAnnotation.readable,
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
