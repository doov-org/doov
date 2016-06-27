/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import static com.datastax.driver.core.DataType.text;
import static com.datastax.driver.core.DataType.timeuuid;
import static com.datastax.driver.core.schemabuilder.SchemaBuilder.Direction.DESC;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;
import static org.modelmap.sample.field.SampleFieldId.EMAIL;
import static org.modelmap.sample.field.SampleFieldId.LOGIN;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmap.core.*;
import org.modelmap.sample.field.SampleTag;
import org.modelmap.sample.model.*;

import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;

public class LiveCode {

    public static void main(String[] args) {
        example1();
        example2();
        exemple3();
    }

    private static void intro() {
        SampleModel model = new SampleModel();
        model.setAccount(new Account());
        model.getAccount().setEmail("parisjug@gmail.com");
        System.out.println(model.getAccount().getEmail());

        FieldModel fieldModel = new SampleModelWrapper(model);
        System.out.println(fieldModel.<String> get(EMAIL));

        fieldModel.set(EMAIL, "parisjug.gmail.com");
        System.out.println(fieldModel.<String> get(EMAIL));
    }

    private static void example1() {
        FieldModel model = SampleModels.wrapper();
        System.out.println(model.<String> get(EMAIL));
        model.stream().forEach(System.out::println);
        Map<FieldId, Object> map = model.stream().collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println(map);
        SampleModelWrapper newModel = new SampleModelWrapper();
        map.entrySet().forEach(e -> newModel.set(e.getKey(), e.getValue()));
        newModel.stream().forEach(System.out::println);
        System.out.println(newModel.getModel().getAccount().getEmail());
    }

    private static void example2() {
        FieldModel model = SampleModels.wrapper();
        Map<FieldId, Object> map = model.stream().collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        SampleModelWrapper newModel = new SampleModelWrapper();
        map.entrySet().stream()
                        .filter(e -> e.getKey().hasTag(SampleTag.ACCOUNT))
                        //.filter(e -> e.getKey().hasTag(SampleTag.USER))
                        .forEach(e -> newModel.set(e.getKey(), e.getValue()));
        newModel.stream().forEach(System.out::println);
    }

    private static void exemple3() {
        FieldModel model = SampleModels.wrapper();
        Create create = SchemaBuilder.createTable("Field")
                        .addClusteringColumn(LOGIN.name(), text())
                        .addPartitionKey("snapshot_id", timeuuid());

        stream(model.getFieldInfos())
                        .filter(f -> !f.id().equals(LOGIN))
                        .forEach(f -> create.addColumn(f.id().name(), cqlType(f)));

        Create.Options createWithOptions = create.withOptions()
                        .clusteringOrder(LOGIN.name(), DESC);
        System.out.println(createWithOptions);

        Insert insert = QueryBuilder.insertInto("Field");
        model.stream().forEach(e -> insert.value(e.getKey().name(), e.getValue()));
        CodecRegistry codecRegistry = new CodecRegistry();
        codecRegistry.register(InstantCodec.instance);

        System.out.println(insert.getQueryString(codecRegistry));
    }

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
}
