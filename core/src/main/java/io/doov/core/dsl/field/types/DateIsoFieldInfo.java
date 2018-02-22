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
package io.doov.core.dsl.field.types;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldId;
import io.doov.core.FieldInfo;
import io.doov.core.dsl.DslField;
import io.doov.core.dsl.DslId;
import io.doov.core.dsl.DslModel;
import io.doov.core.dsl.field.DelegatingFieldInfoImpl;
import io.doov.core.dsl.impl.DefaultCondition;
import io.doov.core.dsl.impl.LocalDateCondition;
import io.doov.core.dsl.impl.TemporalCondition;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.PredicateMetadata;

public class DateIsoFieldInfo extends DelegatingFieldInfoImpl<LocalDate> implements TemporalFieldInfo<LocalDate> {

    public DateIsoFieldInfo(FieldInfo fieldInfo) {
        super(fieldInfo);
    }

    @Override
    public DefaultCondition<LocalDate> getDefaultCondition() {
        return new DateIsoCondition(this);
    }

    @Override
    public TemporalCondition<LocalDate> getTemporalCondition() {
        return new DateIsoCondition(this);
    }

    public static Optional<LocalDate> parse(DslModel model, FieldId id) {
        return Optional.ofNullable(model.<String> get(id)).map(v -> LocalDate.parse(v, BASIC_ISO_DATE));
    }

    private class DateIsoCondition extends LocalDateCondition {

        private DateIsoCondition(DslField field) {
            super(field);
        }

        private DateIsoCondition(DslField field, PredicateMetadata metadata,
                        BiFunction<DslModel, Context, Optional<LocalDate>> value) {
            super(field, metadata, value);
        }

        @Override
        protected TemporalCondition<LocalDate> temporalCondition(DslField field, PredicateMetadata metadata,
                        BiFunction<DslModel, Context, Optional<LocalDate>> value) {
            return new DateIsoCondition(field, metadata, value);
        }

        @Override
        protected Optional<LocalDate> valueModel(DslModel model, DslField field) {
            return parse(model, field.id());
        }

    }

}
