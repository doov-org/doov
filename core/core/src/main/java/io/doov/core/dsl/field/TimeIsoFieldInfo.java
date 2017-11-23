package io.doov.core.dsl.field;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

import java.time.LocalTime;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldId;
import io.doov.core.dsl.*;
import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class TimeIsoFieldInfo extends DefaultFieldInfo<LocalTime> implements TemporalFieldInfo<LocalTime> {

    TimeIsoFieldInfo(FieldId fieldId, String readable, FieldId... siblings) {
        super(fieldId, readable, String.class, new Class<?>[] {}, siblings);
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

        private TimeIsoCondition(DslField field, Metadata metadata,
                        BiFunction<DslModel, Context, Optional<LocalTime>> value) {
            super(field, metadata, value);
        }

        @Override
        protected TemporalCondition<LocalTime> temporalCondition(DslField field, Metadata metadata,
                        BiFunction<DslModel, Context, Optional<LocalTime>> value) {
            return new TimeIsoCondition(field, metadata, value);
        }

        @Override
        protected Optional<LocalTime> valueModel(DslModel model, DslField field) {
            return parse(model, field.id());
        }

    }

}
