package org.modelmap.gen;


import org.apache.commons.lang3.tuple.Pair;
import org.modelmap.core.FieldId;
import org.modelmap.core.PathConstraint;

import java.lang.reflect.Method;
import java.util.List;

final class Visitor {
    private final Class<?> baseClass;
    private final List<VisitorPath> collected;

    public Visitor(Class<?> baseClass, List<VisitorPath> collected) {
        this.baseClass = baseClass;
        this.collected = collected;
    }

    void visit(List<Pair<FieldId, PathConstraint>> fieldTarget, Method getMethod, Method setMethod, List<Method> paths) {
        for (Pair<FieldId, PathConstraint> pair : fieldTarget) {
            if (!checkFieldTargetConstraint(paths, pair.getLeft(), pair.getRight())) {
                continue;
            }
            final VisitorPath path = new VisitorPath(baseClass, paths, pair.getKey(), getMethod, setMethod);
            if (contains(path)) {
                continue;
            }
            collected.add(path);
        }
    }

//    private static EModule parseModule(Class<?> clazz) {
//        for (EModule module : EModule.values()) {
//            if (clazz.getPackage().getName().contains(module.getCode())) {
//                return module;
//            }
//        }
//        return EModule.core;
//    }

    private static boolean checkFieldTargetConstraint(List<Method> paths, FieldId FieldId, PathConstraint constraint) {
//        final PathConstraint constraint = path.constraint();
//        if (isNotEmpty(constraint.includePath())) {
//            final String getterPath = VisitorPath.getterPath(paths, FieldId.position(), false);
//            return getterPath.contains(constraint.includePath());
//        }
        // FIXME use meta-annotations to find
        return true;
    }

    private boolean contains(VisitorPath vPath) {
        final String displayPath = vPath.toString();
        for (VisitorPath aPath : collected) {
            if (aPath.toString().equals(displayPath)) {
                return true;
            }
        }
        return false;
    }
}
