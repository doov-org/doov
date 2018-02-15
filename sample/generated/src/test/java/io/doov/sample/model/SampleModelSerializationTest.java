/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.model;

import static io.doov.sample.model.SampleModels.sample;

import java.io.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.csv.*;

import io.doov.core.*;

public class SampleModelSerializationTest {

    private SampleModelWrapper wrapper;

    @BeforeEach
    void setUp() {
        wrapper = new SampleModelWrapper(sample());
    }

    @Test
    void should_write_fields_to_csv_and_parse_back() {
        ByteArrayOutputStream csvResult = new ByteArrayOutputStream();
        Writer outputWriter = new OutputStreamWriter(csvResult);
        CsvWriter csvWriter = new CsvWriter(outputWriter, new CsvWriterSettings());
        wrapper.getFieldInfos().stream()
                        .filter(f -> !f.isTransient())
                        .forEach(f -> {
                            FieldId fieldId = f.id();
                            csvWriter.writeRow(fieldId.code(), wrapper.getAsString(fieldId));
                        });
        csvWriter.close();
        System.out.println(csvResult.toString());

        SampleModelWrapper copy = new SampleModelWrapper();

        ByteArrayInputStream csvInput = new ByteArrayInputStream(csvResult.toByteArray());
        CsvParser csvParser = new CsvParser(new CsvParserSettings());
        csvParser.parseAll(csvInput).forEach(record -> {
            FieldInfo fieldInfo = fieldInfoByName(record[0], wrapper);
            copy.setAsString(fieldInfo, record[1]);
        });

        SoftAssertions softly = new SoftAssertions();
        wrapper.getFieldInfos().stream()
                        .filter(f -> !f.isTransient())
                        .forEach(f -> {
                            Object value = copy.get(f.id());
                            softly.assertThat(value).isEqualTo(wrapper.get(f.id()));
                        });
        softly.assertAll();
    }

    private static FieldInfo fieldInfoByName(String name, FieldModel model) {
        return model.getFieldInfos().stream().filter(f -> f.id().code().equals(name)).findFirst().orElse(null);
    }

}
