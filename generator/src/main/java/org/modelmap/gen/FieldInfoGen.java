package org.modelmap.gen;


import org.modelmap.core.FieldId;

import java.lang.reflect.Method;
import java.util.*;

import static org.modelmap.gen.ProjetWrapperGen.getterBoxingType;
import static org.modelmap.gen.ProjetWrapperGen.pathGroups;

final class FieldInfoGen {

    // FIXME remove all specific types
    private static boolean isCodeValuable(VisitorPath path) {
//        return isAssignable(path, CodeValuable.class);
        return false;
    }

    // FIXME remove all specific types
    private static boolean isIntValuable(VisitorPath path) {
//        return isAssignable(path, IntValuable.class);
        return false;
    }

    private static boolean isAssignable(VisitorPath path, Class<?> assignableClass) {
        final Method lastMethod = path.getPath().get(path.getPath().size() - 1);
        final Class<?> type = lastMethod.getReturnType();
        return assignableClass.isAssignableFrom(type);
    }

    static String literals(List<FieldId> fieldsOrder, List<VisitorPath> collected) {
        final StringBuilder builder = new StringBuilder();
        final Map<FieldId, List<VisitorPath>> pathGroups = pathGroups(collected);
        for (FieldId FieldId : sortFields(fieldsOrder, pathGroups.keySet())) {
            final VisitorPath currentPath = pathGroups.get(FieldId).get(0);
            String getterBoxingType = getterBoxingType(pathGroups.get(FieldId).get(0), FieldId.position());
            getterBoxingType = getterBoxingType.replace("<", "/*<").replace(">", ">*/");
            builder.append("    ");
            builder.append(FieldId.toString());
            builder.append("(");
            builder.append(FieldId.getClass().getName());
            builder.append(".");
            builder.append(FieldId.toString());
            builder.append(", ");
            builder.append(getterBoxingType);
            builder.append(".class, ");
            builder.append(Boolean.toString(isCodeValuable(pathGroups.get(FieldId).get(0))));
            builder.append(", ");
            builder.append(Boolean.toString(isIntValuable(pathGroups.get(FieldId).get(0))));
            builder.append(formatSiblings(siblings(currentPath, collected)));
            builder.append("), //\n");
        }
        builder.append("    ;");
        return builder.toString();
    }

    private static String formatSiblings(Set<FieldId> siblings) {
        if (siblings.isEmpty())
            return "";
        final StringBuilder builder = new StringBuilder();
        builder.append(", ");
        final Iterator<FieldId> it = siblings.iterator();
        while (it.hasNext()) {
            final FieldId FieldId = it.next();
            builder.append(FieldId.getClass().getName());
            builder.append(".");
            builder.append(FieldId.toString());
            if (it.hasNext())
                builder.append(", ");
        }
        return builder.toString();
    }

    private static Set<FieldId> siblings(VisitorPath currentPath, List<VisitorPath> collected) {
        final String currentCanonicalPath = currentPath.displayPath(true);
        final Set<FieldId> siblings = new HashSet<>();
        for (VisitorPath path : collected) {
            if (path.getFieldId() == currentPath.getFieldId())
                continue;
            if (path.displayPath(true).equals(currentCanonicalPath))
                siblings.add(path.getFieldId());
        }
        return siblings;
    }

    private static List<FieldId> sortFields(final List<FieldId> fieldsOrder, Set<FieldId> FieldIds) {
        final List<FieldId> sortedList = new ArrayList<>(FieldIds);
        sortedList.sort((o1, o2) -> {
            final int p1 = fieldsOrder.indexOf(o1);
            final int p2 = fieldsOrder.indexOf(o2);
            return Integer.compare(p1, p2);
        });
        return sortedList;
    }

}
