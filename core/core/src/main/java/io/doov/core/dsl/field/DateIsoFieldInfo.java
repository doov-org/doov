package io.doov.core.dsl.field;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.BiFunction;

import io.doov.core.FieldId;
import io.doov.core.dsl.*;
import io.doov.core.dsl.impl.*;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.meta.Metadata;

public class DateIsoFieldInfo extends DefaultFieldInfo<LocalDate> implements TemporalFieldInfo<LocalDate> {

    DateIsoFieldInfo(FieldId fieldId, String readable, FieldId... siblings) {
        super(fieldId, readable, String.class, new Class<?>[] {}, siblings);
    }

    @Override
    public DefaultCondition<LocalDate> getDefaultCondition() {
        return new DateIsoCondition(this);
    }

    @Override
    public TemporalCondition<LocalDate> getTemporalCondition() {
        return new DateIsoCondition(this);
    }

    public static Optional<LocalDate> parse(DslModel model, DslId id) {
        return Optional.ofNullable(model.<String> get(id)).map(v -> LocalDate.parse(v, BASIC_ISO_DATE));
    }

    private class DateIsoCondition extends LocalDateCondition {

        private DateIsoCondition(DslField field) {
            super(field);
        }

        private DateIsoCondition(DslField field, Metadata metadata,
                BiFunction<DslModel, Context, Optional<LocalDate>> value) {
            super(field, metadata, value);
        }

        @Override
        protected TemporalCondition<LocalDate> temporalCondition(DslField field, Metadata metadata,
                BiFunction<DslModel, Context, Optional<LocalDate>> value) {
            return new DateIsoCondition(field, metadata, value);
        }

        @Override
        protected Optional<LocalDate> valueModel(DslModel model, DslField field) {
            return parse(model, field.id());
        }

    }

}
