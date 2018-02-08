/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.core.dsl.field;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

import java.time.LocalTime;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldId;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslId;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.LocalTimeCondition;
import io.doov.core.dsl.impl.TemporalCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.PredicateMetadata;

public class TimeIsoFieldInfo extends DefaultFieldInfo<LocalTime> implements TemporalFieldInfo<LocalTime> {

    TimeIsoFieldInfo(FieldId fieldId, String readable, boolean _transient, FieldId... siblings) {
        super(fieldId, readable, String.class, _transient, false, false, new Class<?>[] {}, siblings);
    }

    @Override
    public DefaultCondition<LocalTime> getDefaultCondition() {
        return new TimeIsoCondition(this);
    }

    @Override
    public TemporalCondition<LocalTime> getTemporalCondition() {
        return new TimeIsoCondition(this);
    }

    public static Optional<LocalTime> parse(DslModel model, DslId id) {
        return Optional.ofNullable(model.<String> get(id)).map(v -> LocalTime.parse(v, BASIC_ISO_DATE));
    }

    private class TimeIsoCondition extends LocalTimeCondition {

        private TimeIsoCondition(DslField field) {
            super(field);
        }

        private TimeIsoCondition(DslField field, PredicateMetadata metadata,
                        BiFunction<DslModel, Context, Optional<LocalTime>> value) {
            super(field, metadata, value);
        }

        @Override
        protected TemporalCondition<LocalTime> temporalCondition(DslField field, PredicateMetadata metadata,
                        BiFunction<DslModel, Context, Optional<LocalTime>> value) {
            return new TimeIsoCondition(field, metadata, value);
        }

        @Override
        protected Optional<LocalTime> valueModel(DslModel model, DslField field) {
            return parse(model, field.id());
        }

    }

}
