/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.sample;

import static io.doov.sample.CassandraQueryBuilderTest.codecRegistry;
import static io.doov.sample.CassandraQueryBuilderTest.cqlType;
import static io.doov.sample.CassandraQueryBuilderTest.registerTypeCodecs;
import static io.doov.sample.field.SampleFieldId.EMAIL;
import static io.doov.sample.field.SampleFieldId.LOGIN;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableWithOptions;
import com.datastax.oss.driver.api.querybuilder.term.Term;

import io.doov.core.*;
import io.doov.sample.field.SampleTag;
import io.doov.sample.model.*;
import io.doov.sample.wrapper.SampleModelWrapper;

/**
 * Create a keyspace before starting the live code <tt>
 * CREATE KEYSPACE meetup WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};
 * </tt>
 */
public class LiveCodeCassandraMeetup {

    public static void main(String[] args) {
        intro();
        mixingWithMap();
        tagFiltering();

        cqlCreate();
        cqlInsert();
        cqlAlter();
    }

    static void intro() {
        SampleModel model = new SampleModel();
        model.setAccount(new Account());
        model.getAccount().setEmail("support@lesfurets.com");
        System.out.println(model.getAccount().getEmail());

        FieldModel fieldModel = new SampleModelWrapper(model);
        System.out.println(fieldModel.<String> get(EMAIL));

        fieldModel.set(EMAIL, "gdu@lesfurets.com");
        System.out.println(fieldModel.<String> get(EMAIL));
    }

    static void mixingWithMap() {
        FieldModel model = SampleModels.wrapper();
        System.out.println(model.<String> get(EMAIL));
        model.stream().forEach(System.out::println);

        Map<FieldId, Object> map = model.stream().filter(e -> Objects.nonNull(e.getValue()))
                .collect(toMap(Entry::getKey, Entry::getValue));
        System.out.println(map);

        SampleModelWrapper newModel = map.entrySet().stream().collect(SampleModelWrapper.toFieldModel());
        newModel.stream().forEach(System.out::println);
        System.out.println(newModel.getModel().getAccount().getEmail());
    }

    static void tagFiltering() {
        FieldModel model = SampleModels.wrapper();

        Map<FieldId, Object> map = model.stream().collect(toMap(Entry::getKey, Entry::getValue));
        SampleModelWrapper newModel = map.entrySet().stream()
                .filter(e -> e.getKey().hasTag(SampleTag.ACCOUNT))
                // .filter(e -> e.getKey().hasTag(SampleTag.USER))
                .collect(SampleModelWrapper.toFieldModel());

        newModel.stream().forEach(System.out::println);
    }

    static void cqlCreate() {
        FieldModel model = SampleModels.wrapper();

        CreateTable createTable = SchemaBuilder.createTable("sample", "model")
                .withPartitionKey("snapshot_id", DataTypes.TIMEUUID)
                .withClusteringColumn(LOGIN.name(), DataTypes.TEXT);

        for (FieldInfo info : model.getFieldInfos().stream()
                .filter(info -> info.id() != LOGIN)
                .toArray(FieldInfo[]::new)) {
            createTable = createTable.withColumn(info.id().code(), cqlType(info));
        }

        CreateTableWithOptions createTableWithOptions = createTable.withClusteringOrder(LOGIN.name(),
                ClusteringOrder.DESC);

        execute(createTableWithOptions.build());
    }

    static void cqlInsert() {
        FieldModel model = SampleModels.wrapper();

        Map<String, Term> values = model.stream().collect(Collectors.toMap(
                e -> e.getKey().code(),
                e -> QueryBuilder.literal(e.getValue(), codecRegistry())));
        Insert insertInto = QueryBuilder.insertInto("sample", "model")
                .value("snapshot_id", QueryBuilder.literal(Uuids.timeBased()))
                .values(values);

        execute(insertInto.build());
    }

    static void cqlAlter() {
        FieldModel model = SampleModels.wrapper();
        try (CqlSession session = CqlSession.builder().build()) {
            model.getFieldInfos().stream()
                    .filter(f -> !session.getMetadata()
                            .getKeyspace("sample").get()
                            .getTable("model").get()
                            .getColumn(f.id().code()).isPresent())
                    .forEach(f -> session.execute(SchemaBuilder.alterTable("sample", "model")
                            .addColumn(f.id().code(), cqlType(f)).build()));
        }
    }

    static void execute(SimpleStatement statement) {
        try (CqlSession session = session()) {
            session.execute(statement);
        }
    }

    static CqlSession session() {
        return registerTypeCodecs(CqlSession.builder().build());
    }
}
