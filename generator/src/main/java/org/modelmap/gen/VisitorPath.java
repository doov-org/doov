package org.modelmap.gen;

import org.modelmap.core.FieldId;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.uncapitalize;

public final class VisitorPath {
    private final Class<?> baseClass;
    private final List<Method> path;
    private final FieldId fieldId;
    private final Method getMethod;
    private final Method setMethod;

    public VisitorPath(Class<?> baseClass, List<Method> getPath, FieldId fieldId, Method getMethod, Method setMethod) {
        this.baseClass = baseClass;
        this.path = new ArrayList<>(getPath);
        this.fieldId = fieldId;
        this.getMethod = getMethod;
        this.setMethod = setMethod;
    }

    public Class<?> getBaseClass() {
        return baseClass;
    }

    public List<Method> getPath() {
        return path;
    }

    public boolean containsList() {
        for (int i = 0; i < path.size() - 1; i++) {
            if (List.class.isAssignableFrom(path.get(i).getReturnType())) {
                return true;
            }
        }
        return false;
    }

    public boolean containsGenerics() {
        for (int i = 0; i < path.size() - 1; i++) {
            if (path.get(i).getGenericReturnType().toString().contains("<")) {
                return true;
            }
        }
        return false;
    }

    public FieldId getFieldId() {
        return fieldId;
    }

    public Method getGetMethod() {
        return getMethod;
    }

    public Method getSetMethod() {
        return setMethod;
    }

    public String displayPath(boolean canonical) {
        return getterPath(path, fieldId.position(), canonical);
    }

    @Override
    public String toString() {
        return uncapitalize(baseClass.getSimpleName()) + "." + displayPath(false) + ":" + fieldId;
    }

    /**
     * FIXME keep CSV generation?
     */
    @Deprecated
    public String toCsv() {
        return uncapitalize(baseClass.getSimpleName()) + "." + displayPath(false) + ';'
                + fieldId + ';'
                + getMethod.getReturnType().getSimpleName() + '\n';
    }

    static String getterPath(List<Method> path, boolean canonical) {
        return getterPath(path, -1, canonical);
    }

    static String getterPath(List<Method> path, int index, boolean canonical) {
        final StringBuilder buffer = new StringBuilder();
        for (Method method : path) {
            if (List.class.isAssignableFrom(method.getReturnType()) && index >= 0) {
                buffer.append(method.getName());
                buffer.append("().get(");
                buffer.append(index - 1);
                buffer.append(")");
            } else {
                buffer.append(canonical ? canonicalName(method) : method.getName());
                buffer.append("()");
            }
            if (path.indexOf(method) < path.size() - 1) {
                buffer.append('.');
            }
        }
        return buffer.toString();
    }

    /**
     * FIXME remove specific code
     */
    @Deprecated
    static String canonicalName(Method method) {
        if ("getContactClient".equals(method.getName()))
            return "getA";
        if ("getSouscripteur".equals(method.getName()))
            return "getA";
        if ("getConducteurPrincipal".equals(method.getName()))
            return "getA";
        if ("getEmprunteur".equals(method.getName()))
            return "getA";
        if ("getConducteur".equals(method.getName()))
            return "getA";
        if ("getAssure".equals(method.getName()))
            return "getA";
        if ("getAdherent".equals(method.getName()))
            return "getA";
        if ("getToyClaimer".equals(method.getName()))
            return "getA";
        return method.getName();
    }
}
