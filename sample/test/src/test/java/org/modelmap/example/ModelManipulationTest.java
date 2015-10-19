/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
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

        /* stream all key-values pair from both models */
        Stream.concat(sampleV1.parallelStream().map(ValueDifference::left),
                        sampleV2.parallelStream().map(ValueDifference::right))

                        /* merging key-value pair in a map */
                        .collect(Collectors.toMap(ValueDifference::getKey, diff -> diff, ValueDifference::merge))

                        /* filter to keep only key with 2 differents values */
                        .values().stream().filter(diff -> !diff.isEquals())

                        /* print keys with differents values */
                        .forEach(System.out::println);
    }

}
