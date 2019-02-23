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
package io.doov.core.dsl.field;

import static java.util.Collections.unmodifiableMap;

import java.time.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

import io.doov.core.FieldInfo;
import io.doov.core.dsl.field.types.*;

/**
 * Default implementation for provided types.
 */
public class FieldTypes implements FieldTypeProvider {

    private static final Map<Predicate<FieldInfo>, Class<? extends FieldInfo>> TYPES;

    static {
        Map<Predicate<FieldInfo>, Class<? extends FieldInfo>> map = new LinkedHashMap<>();
        map.put((f) -> f.type().equals(Boolean.class) || f.type().equals(Boolean.TYPE), BooleanFieldInfo.class);
        map.put((f) -> f.type().equals(Character.class) || f.type().equals(Character.TYPE), CharacterFieldInfo.class);
        map.put((f) -> f.type().equals(Double.class) || f.type().equals(Double.TYPE), DoubleFieldInfo.class);
        map.put((f) -> Enum.class.isAssignableFrom(f.type()) || f.isCodeValuable(), EnumFieldInfo.class);
        map.put((f) -> f.type().equals(Float.class) || f.type().equals(Float.TYPE), FloatFieldInfo.class);
        map.put((f) -> f.type().equals(Integer.class) || f.type().equals(Integer.TYPE), IntegerFieldInfo.class);
        map.put((f) -> f.type().equals(Long.class) || f.type().equals(Long.TYPE), LongFieldInfo.class);
        map.put((f) -> Iterable.class.isAssignableFrom(f.type()), IterableFieldInfo.class);
        map.put((f) -> f.type().equals(LocalDate.class), LocalDateFieldInfo.class);
        map.put((f) -> f.type().equals(LocalTime.class), LocalTimeFieldInfo.class);
        map.put((f) -> f.type().equals(LocalDateTime.class), LocalDateTimeFieldInfo.class);
        map.put((f) -> f.type().equals(String.class), StringFieldInfo.class);
        TYPES = unmodifiableMap(map);
    }

    @Override
    public Map<Predicate<FieldInfo>, Class<? extends FieldInfo>> getTypes() {
        return TYPES;
    }

}
