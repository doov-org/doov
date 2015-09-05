package org.modelmap.gen;


import org.modelmap.core.FieldId;
import org.modelmap.core.FieldTarget;
import org.modelmap.core.Path;
import org.modelmap.core.PathConstraint;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

final class Visitor {
    private final Class<?> baseClass;
    private final List<VisitorPath> collected;

    public Visitor(Class<?> baseClass, List<VisitorPath> collected) {
        this.baseClass = baseClass;
        this.collected = collected;
    }

    void visit(FieldTarget fieldTarget, Method getMethod, Method setMethod, boolean isTransient, List<Method> paths) {
        for (Path path : fieldTarget.value()) {
            final Collection<FieldId> fields = collect(path);
            if (fields.isEmpty()) {
                continue;
            }
            for (FieldId FieldId : fields) {
                if (!checkFieldTargetConstraint(path, paths, FieldId)) {
                    continue;
                }
                final VisitorPath vPath = new VisitorPath(baseClass, paths, FieldId, getMethod, setMethod, isTransient);
                if (contains(vPath)) {
                    continue;
                }
                collected.add(vPath);
            }
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

    // FIXME use meta-annotations to collect fields
    private static Collection<FieldId> collect(Path path) {
        final Collection<FieldId> fields = new ArrayList<>();
//        fields.addAll(asList(path.coordonnees()));
//        fields.addAll(asList(path.core()));
//        switch (module) {
//            case auto:
//                fields.addAll(asList(path.auto()));
//                break;
//            case extra:
//                fields.addAll(asList(path.contact()));
//                fields.addAll(asList(path.resiliation()));
//                fields.addAll(asList(path.switching()));
//                fields.addAll(asList(path.rewards()));
//                break;
//            case emprunteur:
//                fields.addAll(asList(path.emprunteur()));
//                break;
//            case moto:
//                fields.addAll(asList(path.moto()));
//                break;
//            case mrh:
//                fields.addAll(asList(path.mrh()));
//                break;
//            case sante:
//                fields.addAll(asList(path.sante()));
//                break;
//            case telco:
//                fields.addAll(asList(path.telco()));
//                break;
//            default:
//                throw new IllegalArgumentException(module.toString());
//        }
        return fields;
    }

    private static boolean checkFieldTargetConstraint(Path path, List<Method> paths, FieldId FieldId) {
        final PathConstraint constraint = path.constraint();
        if (isNotEmpty(constraint.includePath())) {
            final String getterPath = VisitorPath.getterPath(paths, FieldId.position(), false);
            return getterPath.contains(constraint.includePath());
        }
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
