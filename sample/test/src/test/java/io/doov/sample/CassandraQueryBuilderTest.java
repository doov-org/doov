/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.doov.sample;

import static com.datastax.driver.core.DataType.text;
import static com.datastax.driver.core.DataType.timeuuid;
import static com.datastax.driver.core.schemabuilder.SchemaBuilder.Direction.DESC;
import static io.doov.sample.field.SampleFieldId.LOGIN;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.Create.Options;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.extras.codecs.jdk8.LocalDateCodec;

import io.doov.core.FieldInfo;
import io.doov.core.FieldModel;
import io.doov.sample.model.SampleModels;

public class CassandraQueryBuilderTest {

    private static DataType cqlType(FieldInfo info) {
        if (String.class.equals(info.type())) {
            return DataType.text();
        } else if (Boolean.class.equals(info.type()) || Boolean.TYPE.equals(info.type())) {
            return DataType.cboolean();
        } else if (Long.class.equals(info.type()) || Long.TYPE.equals(info.type())) {
            return DataType.cint();
        } else if (Integer.class.equals(info.type()) || Integer.TYPE.equals(info.type())) {
            return DataType.cint();
        } else if (LocalDate.class.equals(info.type())) {
            return DataType.date();
        } else if (Enum.class.isAssignableFrom(info.type())) {
            return DataType.set(DataType.text());
        } else if (Collection.class.isAssignableFrom(info.type())) {
            return DataType.set(DataType.text());
        }
        throw new IllegalArgumentException("unknown type " + info.type() + " for " + info.id());
    }

    private static CodecRegistry codecRegistry() {
        CodecRegistry registry = new CodecRegistry();
        registry.register(LocalDateCodec.instance);
        return registry;
    }

    @Test
    public void simpleCassandraSchema() {
        FieldModel model = SampleModels.wrapper();

        Create createRequest = SchemaBuilder.createTable("fields_model")
                .addClusteringColumn(LOGIN.name(), text())
                .addPartitionKey("snapshot_id", timeuuid());

        model.getFieldInfos().stream()
                .filter(info -> info.id() != LOGIN)
                .forEach(info -> createRequest.addColumn(info.id().name(), cqlType(info)));

        Options createRequestWithOptions = createRequest.withOptions().clusteringOrder(LOGIN.name(), DESC);
        print(createRequestWithOptions.getQueryString());
    }

    @Test
    public void simpleCasandraInsert() {
        FieldModel model = SampleModels.wrapper();
        Insert insertRequest = QueryBuilder.insertInto("fields_model");
        insertRequest.value("snapshot_id", UUID.randomUUID());
        insertRequest.values(
                model.stream().map(e -> e.getKey().name()).collect(toList()),
                model.stream().map(Entry::getValue).collect(toList()));
        print(insertRequest.getQueryString(codecRegistry()));
    }

    private static void print(String string) {
        if (System.getProperty("activateSystemOut") != null) {
            System.out.println(string);
        }
    }

}
