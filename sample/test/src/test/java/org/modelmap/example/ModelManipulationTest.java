package org.modelmap.example;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.Test;
import org.modelmap.core.FieldId;
import org.modelmap.core.FieldModel;
import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.model.SampleModel;
import org.modelmap.sample.model.SampleModelWrapper;
import org.modelmap.sample.model.SampleModels;

public class ModelManipulationTest {

    @Test
    public void mixingWithMap() {
        SampleModel sample = SampleModels.sample();
        System.out.println(sample.getUser().getFullName());
        Map<FieldId, Object> aMap = new SampleModelWrapper(sample).stream()
                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        SampleModel clone = aMap.entrySet().stream().collect(SampleModelWrapper.toFieldModel()).getModel();
        System.out.println(clone.getUser().getFullName());
    }

    @Test
    public void csv() {
        SampleModel sample = SampleModels.sample();
        String csv = new SampleModelWrapper(sample).parallelStream()
                        .map(e -> e.getKey() + ";" + String.valueOf(e.getValue()) + "\n").reduce("", String::concat);

        System.out.println(csv);
    }

    @Test
    public void json() {
        SampleModel sample = SampleModels.sample();
        String jsonValues = new SampleModelWrapper(sample).parallelStream()
                        .map(e -> "  \"" + e.getKey() + "=" + String.valueOf(e.getValue()) + "\"\n")
                        .reduce("", String::concat);
        String json = "{\n" + jsonValues + "\n}";

        System.out.println(json);
    }

    @Test
    public void diff() {
        FieldModel sample_1 = SampleModels.wrapper();
        FieldModel sample_2 = SampleModels.wrapper();

        sample_1.set(SampleFieldId.FAVORITE_SITE_NAME_3, null);
        sample_1.set(SampleFieldId.FAVORITE_SITE_URL_3, null);
        sample_2.set(SampleFieldId.FAVORITE_SITE_NAME_1, "LesFurets.com");
        sample_2.set(SampleFieldId.FAVORITE_SITE_URL_1, "www.lesfurets.com");
        sample_2.set(SampleFieldId.EMAILS_PREFERENCES, Collections.emptyList());

        /* stream all key-values pair from both models */
        Stream.concat(sample_1.stream().map(buildRight), sample_2.stream().map(buildLeft))

                        /* merging key-value pair in a map */
                        .collect(Collectors.toMap(Triple::getMiddle, Function.identity(), merge))

                        /* filter to keep only key with 2 differents values */
                        .values().stream().filter(isNotSame)

                        /* print keys with differents values */
                        .forEach(System.out::println);
    }

    static Function<Entry<FieldId, Object>, Triple<Object, FieldId, Object>> buildLeft = (entry) -> {
        return Triple.of(entry.getValue(), entry.getKey(), null);
    };

    static Function<Entry<FieldId, Object>, Triple<Object, FieldId, Object>> buildRight = (entry) -> {
        return Triple.of(null, entry.getKey(), entry.getValue());
    };

    static Predicate<Triple<Object, FieldId, Object>> isNotSame = (triple) -> {
        return !Objects.equals(triple.getLeft(), triple.getRight());
    };

    static BinaryOperator<Triple<Object, FieldId, Object>> merge = (t1, t2) -> {
        Object left = t1.getLeft() != null ? t1.getLeft() : t2.getLeft();
        Object right = t2.getRight() != null ? t2.getRight() : t1.getRight();
        return Triple.of(left, t1.getMiddle(), right);
    };
}
