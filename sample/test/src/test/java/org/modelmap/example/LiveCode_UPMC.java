package org.modelmap.example;

import static com.datastax.driver.core.DataType.text;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.*;

import org.apache.commons.lang3.tuple.Triple;
import org.modelmap.core.FieldId;
import org.modelmap.core.FieldInfo;

import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.DataType;
import com.datastax.driver.extras.codecs.jdk8.LocalDateCodec;

public class LiveCode_UPMC {

    public static void main(String[] args) {
    }

    @SuppressWarnings("unused")
    private static CodecRegistry codecRegistry() {
        final CodecRegistry registry = new CodecRegistry();
        registry.register(LocalDateCodec.instance);
        return registry;
    }

    @SuppressWarnings("unused")
    private static DataType cqlType(FieldInfo info) {
        if (String.class.equals(info.type()))
            return text();
        else if (Boolean.class.equals(info.type()) || Boolean.TYPE.equals(info.type()))
            return DataType.cboolean();
        else if (Long.class.equals(info.type()) || Long.TYPE.equals(info.type()))
            return DataType.cint();
        else if (LocalDate.class.equals(info.type()))
            return DataType.date();
        else if (Enum.class.isAssignableFrom(info.type()))
            return DataType.set(text());
        else if (Collection.class.isAssignableFrom(info.type()))
            return DataType.set(text());
        throw new IllegalArgumentException("unknown type " + info.type() + " for " + info.id());
    }

    private static Function<Entry<FieldId, Object>, Triple<Object, FieldId, Object>> buildLeft = (entry) ->
                    Triple.of(entry.getValue(), entry.getKey(), null);

    private static Function<Entry<FieldId, Object>, Triple<Object, FieldId, Object>> buildRight = (entry) ->
                    Triple.of(null, entry.getKey(), entry.getValue());

    private static Predicate<Triple<Object, FieldId, Object>> isNotSame = (triple) ->
                    !Objects.equals(triple.getLeft(), triple.getRight());

    private static BinaryOperator<Triple<Object, FieldId, Object>> merge = (t1, t2) -> {
        Object left = t1.getLeft() != null ? t1.getLeft() : t2.getLeft();
        Object right = t2.getRight() != null ? t2.getRight() : t1.getRight();
        return Triple.of(left, t1.getMiddle(), right);
    };
}
