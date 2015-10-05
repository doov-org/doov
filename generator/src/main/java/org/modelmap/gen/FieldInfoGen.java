package org.modelmap.gen;

import static org.modelmap.gen.ModelWrapperGen.getterBoxingType;

import java.util.*;

import org.modelmap.core.FieldId;

final class FieldInfoGen {

    static String literals(Map<FieldId, VisitorPath> fieldPaths) {
        final StringBuilder builder = new StringBuilder();
        fieldPaths.keySet().stream()
                        .sorted((f1, f2) -> f1.name().compareTo(f2.name()))
                        .forEach(fieldId -> {
                            VisitorPath currentPath = fieldPaths.get(fieldId);
                            String getterBoxingType = getterBoxingType(fieldPaths.get(fieldId), fieldId.position());
                            getterBoxingType = getterBoxingType.replace("<", "/*<").replace(">", ">*/");
                            builder.append("    ");
                            builder.append(fieldId.toString());
                            builder.append("(");
                            builder.append(fieldId.getClass().getName());
                            builder.append(".");
                            builder.append(fieldId.toString());
                            builder.append(", ");
                            builder.append(getterBoxingType);
                            builder.append(".class ");
                            builder.append(formatSiblings(siblings(currentPath, fieldPaths.values())));
                            builder.append("), //\n");
                        });
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

    private static Set<FieldId> siblings(VisitorPath currentPath, Collection<VisitorPath> collected) {
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
}
