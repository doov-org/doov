/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.junit.Test;
import org.modelmap.core.FieldId;
import org.modelmap.core.FieldModel;
import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.model.*;
import org.modelmap.sample2.model.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ModelManipulationTest {

    @Test
    public void cvs() {
        SampleModel sample = SampleModels.sample();
        String csv = new SampleModelWrapper(sample).parallelStream()
                        .map(e -> e.getKey() + ";" + String.valueOf(e.getValue()) + "\n")
                        .reduce("", String::concat);

        //        System.out.println(csv);
    }

    @Test
    public void diff() {
        FieldModel sampleV1 = SampleModels.wrapper();
        FieldModel samplev2 = SampleModels.wrapper();

        samplev2.set(SampleFieldId.FAVORITE_SITE_NAME_1, "LesFurets.com");
        samplev2.set(SampleFieldId.FAVORITE_SITE_URL_1, "www.lesfurets.com");
        samplev2.set(SampleFieldId.EMAILS_PREFERENCES, Collections.emptyList());

        String diff = sampleV1.parallelStream()
                        .filter(e -> !Objects.equals(e.getValue(), samplev2.get(e.getKey())))
                        .map(e -> e.getKey() + ";" + sampleV1.get(e.getKey()) + ";" + samplev2.get(e.getKey()) + "\n")
                        .reduce("", String::concat);

        //        System.out.println(diff);
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void json() throws JsonProcessingException {

        Sample2Model sample = Sample2Models.sample();

        Map<FieldId, String> values = new Sample2ModelWrapper(sample).stream()
                        .collect(Collectors.toMap(Entry::getKey, e -> String.valueOf(e.getValue())));

        OBJECT_MAPPER.writeValueAsString(values);

        //        System.out.println(OBJECT_MAPPER.writeValueAsString(values));
    }
}
