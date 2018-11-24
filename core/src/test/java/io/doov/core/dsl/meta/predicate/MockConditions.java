/*
 * Copyright 2018 Courtanet
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
package io.doov.core.dsl.meta.predicate;

import static io.doov.core.dsl.runtime.FieldChainBuilder.from;

import java.time.*;
import java.util.*;

import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.types.*;
import io.doov.core.dsl.impl.DefaultStepCondition;
import io.doov.core.dsl.lang.StepCondition;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataVisitor;
import io.doov.core.dsl.meta.predicate.LeafPredicateMetadata;
import io.doov.core.dsl.runtime.*;

class MockConditions {

    static RuntimeFieldRegistry FIELDS = new RuntimeFieldRegistry(Arrays.asList());

    static void reset() {
        FIELDS = new RuntimeFieldRegistry(Arrays.asList());
    }

    static StepCondition alwaysTrue(String readable) {
        return new DefaultStepCondition(LeafPredicateMetadata.trueMetadata().valueReadable(() -> readable),
                (model, context) -> true);
    }

    static StepCondition alwaysFalse(String readable) {
        return new DefaultStepCondition(LeafPredicateMetadata.falseMetadata().valueReadable(() -> readable),
                (model, context) -> false);
    }

    private static <T> RuntimeField<Object, T> runtimeModel(T value, String readable) {
        RuntimeField<Object, T> field = from(Object.class, () -> readable).readable(readable).field(o -> value,
                (o, v) ->
                {
                }, (Class<T>) value.getClass());
        List<RuntimeField> list = FIELDS.runtimeFields();
        list.add(field);
        FIELDS = new RuntimeFieldRegistry(list);
        return field;
    }

    static BooleanFieldInfo booleanField(boolean value, String readable) {
        return new BooleanFieldInfo(runtimeModel(value, readable));
    }

    static CharacterFieldInfo charField(char value, String readable) {
        return new CharacterFieldInfo(runtimeModel(value, readable));
    }

    static DoubleFieldInfo doubleField(double value, String readable) {
        return new DoubleFieldInfo(runtimeModel(value, readable));
    }

    static <E extends Enum<E>> EnumFieldInfo<E> enumField(E value, String readable) {
        return new EnumFieldInfo<>(runtimeModel(value, readable));
    }

    static FloatFieldInfo floatField(float value, String readable) {
        return new FloatFieldInfo(runtimeModel(value, readable));
    }

    static IntegerFieldInfo intField(int value, String readable) {
        return new IntegerFieldInfo(runtimeModel(value, readable));
    }

    static LocalDateFieldInfo localDateField(LocalDate value, String readable) {
        return new LocalDateFieldInfo(runtimeModel(value, readable));
    }

    static LocalDateTimeFieldInfo localDateTimeField(LocalDateTime value, String readable) {
        return new LocalDateTimeFieldInfo(runtimeModel(value, readable));
    }

    static LocalTimeFieldInfo localTimeField(LocalTime value, String readable) {
        return new LocalTimeFieldInfo(runtimeModel(value, readable));
    }

    static LongFieldInfo longField(long value, String readable) {
        return new LongFieldInfo(runtimeModel(value, readable));
    }

    static StringFieldInfo stringField(String value, String readable) {
        return new StringFieldInfo(runtimeModel(value, readable));
    }

    static <T, C extends Iterable<T>> IterableFieldInfo<T, C> iterableField(C value, String readable) {
        return new IterableFieldInfo<>(runtimeModel(value, readable));
    }

    static DslModel model() {
        return new RuntimeModel<>(FIELDS, new Object());
    }

    static Set<Metadata> collectMetadata(Metadata root) {
        HashSet<Metadata> metadatas = new HashSet<>();
        MetadataVisitor visitor = new MetadataVisitor() {
            @Override
            public void start(Metadata metadata, int depth) {
                metadatas.add(metadata);
            }
        };
        visitor.browse(root, 0);
        return metadatas;
    }
}
