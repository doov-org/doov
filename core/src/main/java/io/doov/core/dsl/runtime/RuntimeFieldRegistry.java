package io.doov.core.dsl.runtime;

import static java.util.Comparator.comparing;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;

/**
 * Runtime field registry optimized for searching {@link RuntimeField}s via Enum {@link FieldId}s.
 * This implementation initializes a list of enum maps according to FieldId's of given list of RuntimeFields.
 *
 * @param <M> model entry type
 */
public class RuntimeFieldRegistry<M> {

    private final List<RuntimeField<M, Object>> runtimeFields;
    private final List<Class<?>> fieldIdTypes;
    private final List<EnumMap<?, RuntimeField<M, Object>>> enumFieldMaps;
    private final Map<FieldId, RuntimeField<M, Object>> nonEnumFieldMap;

    @SuppressWarnings("unchecked")
    public RuntimeFieldRegistry(List<RuntimeField<M, Object>> runtimeFieldList) {
        runtimeFields = new ArrayList<>();
        fieldIdTypes = new ArrayList<>();
        enumFieldMaps = new ArrayList<>();
        nonEnumFieldMap = new HashMap<>();
        runtimeFieldList.stream()
                        .sorted(comparing(r -> r.id().code()))
                        .peek(runtimeFields::add)
                        .collect(Collectors.groupingBy(r -> r.id().getClass()))
                        .forEach((c, fields) -> {
                            if (c.isEnum()) {
                                fieldIdTypes.add(c);
                                final EnumMap enumMap = new EnumMap(c);
                                fields.forEach(f -> enumMap.put((Enum) f.id(), f));
                                enumFieldMaps.add(enumMap);
                            } else {
                                fields.forEach(f -> nonEnumFieldMap.put(f.id(), f));
                            }
                        });
    }

    @SuppressWarnings("unchecked")
    public List<FieldInfo> fieldInfos() {
        return (List) runtimeFields;
    }

    public List<RuntimeField<M, Object>> runtimeFields() {
        return runtimeFields;
    }

    public Stream<RuntimeField<M, Object>> stream() {
        return runtimeFields.stream();
    }

    public RuntimeField<M, Object> get(FieldId fieldId) {
        int classIndex = -1;
        for (int i = 0; i < fieldIdTypes.size(); i++) {
            Class<?> type = fieldIdTypes.get(i);
            if (fieldId.getClass() == type) {
                classIndex = i;
                break;
            }
        }
        if (classIndex >= 0) {
            return enumFieldMaps.get(classIndex).get(fieldId);
        }
        return nonEnumFieldMap.getOrDefault(fieldId, null);
    }

}
