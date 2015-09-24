package org.modelmap.gen;

import static org.modelmap.gen.ModelWrapperGen.getterBoxingType;
import static org.modelmap.gen.ModelWrapperGen.pathGroups;

import java.util.*;

import org.modelmap.core.FieldId;

final class FieldInfoGen {

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
            builder.append(".class ");
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
        final String currentCanonicalPath = currentPath.displayPath();
        final Set<FieldId> siblings = new HashSet<>();
        for (VisitorPath path : collected) {
            if (path.getFieldId() == currentPath.getFieldId())
                continue;
            if (path.displayPath().equals(currentCanonicalPath))
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
