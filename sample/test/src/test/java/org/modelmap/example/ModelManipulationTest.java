/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.modelmap.core.FieldId;
import org.modelmap.core.FieldModel;
import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.model.*;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ModelManipulationTest {

    @Test
    public void cvs() {
        SampleModel sample = SampleModels.sample();
        String csv = new SampleModelWrapper(sample).parallelStream()
                        .map(e -> e.getKey() + ";" + String.valueOf(e.getValue()) + "\n")
                        .reduce("", String::concat);

        System.out.println(csv);
    }

    @Test
    public void json() throws JsonProcessingException {
        SampleModel sample = SampleModels.sample();
        String jsonValues = new SampleModelWrapper(sample).parallelStream()
                        .map(e -> "  \"" + e.getKey() + "=" + String.valueOf(e.getValue()) + "\"\n")
                        .reduce("", String::concat);
        String json = "{\n" + jsonValues + "\n}";

        System.out.println(json);
    }

    @Test
    public void diff() {
        FieldModel sampleV1 = SampleModels.wrapper();
        FieldModel sampleV2 = SampleModels.wrapper();

        sampleV1.set(SampleFieldId.FAVORITE_SITE_NAME_3, null);
        sampleV1.set(SampleFieldId.FAVORITE_SITE_URL_3, null);

        sampleV2.set(SampleFieldId.FAVORITE_SITE_NAME_1, "LesFurets.com");
        sampleV2.set(SampleFieldId.FAVORITE_SITE_URL_1, "www.lesfurets.com");
        sampleV2.set(SampleFieldId.EMAILS_PREFERENCES, Collections.emptyList());

        Stream.concat(sampleV1.stream().map(ValueDifference::left), sampleV2.stream().map(ValueDifference::right))
                        .collect(Collectors.toMap(diff -> diff.fieldId, diff -> diff, ValueDifference::merge))
                        .entrySet().stream()
                        .filter(e -> !e.getValue().isEquals())
                        .forEach(e -> System.out.println(e.getValue()));
    }

    private static final class ValueDifference {

        static ValueDifference left(Entry<FieldId, Object> entry) {
            ValueDifference diff = new ValueDifference(entry.getKey());
            diff.left = entry.getValue();
            return diff;
        }

        static ValueDifference right(Entry<FieldId, Object> entry) {
            ValueDifference diff = new ValueDifference(entry.getKey());
            diff.right = entry.getValue();
            return diff;
        }

        static ValueDifference merge(ValueDifference d1, ValueDifference d2) {
            if (d2.left != null)
                d1.left = d2.left;
            if (d2.right != null)
                d1.right = d2.right;
            return d1;
        }

        final FieldId fieldId;
        Object left;
        Object right;

        ValueDifference(FieldId fieldId) {
            this.fieldId = fieldId;
        }

        boolean isEquals() {
            return Objects.equals(left, right);
        }

        @Override
        public String toString() {
            return fieldId + ";" + String.valueOf(left) + ";" + String.valueOf(right);
        }
    }
}
