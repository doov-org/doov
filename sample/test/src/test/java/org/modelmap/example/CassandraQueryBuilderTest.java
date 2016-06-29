/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import static com.datastax.driver.core.DataType.text;
import static com.datastax.driver.core.DataType.timeuuid;
import static com.datastax.driver.core.schemabuilder.SchemaBuilder.Direction.DESC;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.modelmap.sample.field.SampleFieldId.LOGIN;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.Test;
import org.modelmap.core.FieldInfo;
import org.modelmap.core.FieldModel;
import org.modelmap.sample.model.SampleModels;

import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.Create.Options;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.extras.codecs.jdk8.LocalDateCodec;

public class CassandraQueryBuilderTest {

    private static DataType cqlType(FieldInfo info) {
        if (String.class.equals(info.type()))
            return DataType.text();
        else if (Boolean.class.equals(info.type()) || Boolean.TYPE.equals(info.type()))
            return DataType.cboolean();
        else if (Long.class.equals(info.type()) || Long.TYPE.equals(info.type()))
            return DataType.cint();
        else if (LocalDate.class.equals(info.type()))
            return DataType.date();
        else if (Enum.class.isAssignableFrom(info.type()))
            return DataType.set(DataType.text());
        else if (Collection.class.isAssignableFrom(info.type()))
            return DataType.set(DataType.text());
        throw new IllegalArgumentException("unknown type " + info.type() + " for " + info.id());
    }

    private static CodecRegistry codecRegistry() {
        final CodecRegistry registry = new CodecRegistry();
        registry.register(LocalDateCodec.instance);
        return registry;
    }

    @Test
    public void simpleCassandraSchema() {
        FieldModel model = SampleModels.wrapper();
        Create createRequest = SchemaBuilder.createTable("fields_model").addClusteringColumn(LOGIN.name(), text())
                        .addPartitionKey("snapshot_id", timeuuid());
        stream(model.getFieldInfos()).filter(info -> info.id() != LOGIN).forEach(info -> {
            createRequest.addColumn(info.id().name(), cqlType(info));
        });
        Options createRequestWithOptions = createRequest.withOptions().clusteringOrder(LOGIN.name(), DESC);
        System.out.println(createRequestWithOptions.getQueryString());
    }

    @Test
    public void simpleCasandraInsert() {
        FieldModel model = SampleModels.wrapper();
        Insert insertRequest = QueryBuilder.insertInto("fields_model");
        insertRequest.value("snapshot_id", UUID.randomUUID());
        insertRequest.values(model.stream().map(e -> e.getKey().name()).collect(toList()),
                        model.stream().map(Entry::getValue).collect(toList()));
        System.out.println(insertRequest.getQueryString(codecRegistry()));
    }
}
