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

import static io.doov.sample.field.SampleFieldId.LOGIN;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.api.core.type.codec.registry.MutableCodecRegistry;
import com.datastax.oss.driver.api.core.uuid.Uuids;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTableWithOptions;
import com.datastax.oss.driver.api.querybuilder.term.Term;
import com.datastax.oss.driver.internal.core.type.codec.registry.DefaultCodecRegistry;

import io.doov.core.FieldInfo;
import io.doov.core.FieldModel;
import io.doov.sample.model.*;

public class CassandraQueryBuilderTest {

    static DataType cqlType(FieldInfo info) {
        if (String.class.equals(info.type())) {
            return DataTypes.TEXT;
        } else if (Boolean.class.equals(info.type()) || Boolean.TYPE.equals(info.type())) {
            return DataTypes.BOOLEAN;
        } else if (Long.class.equals(info.type()) || Long.TYPE.equals(info.type())) {
            return DataTypes.BIGINT;
        } else if (Double.class.equals(info.type()) || Double.TYPE.equals(info.type())) {
            return DataTypes.DOUBLE;
        } else if (Float.class.equals(info.type()) || Float.TYPE.equals(info.type())) {
            return DataTypes.FLOAT;
        } else if (Integer.class.equals(info.type()) || Integer.TYPE.equals(info.type())) {
            return DataTypes.INT;
        } else if (LocalDate.class.equals(info.type())) {
            return DataTypes.DATE;
        } else if (Enum.class.isAssignableFrom(info.type())) {
            return DataTypes.TEXT;
        } else if (Collection.class.isAssignableFrom(info.type())) {
            return DataTypes.setOf(DataTypes.TEXT);
        }
        throw new IllegalArgumentException("unknown type " + info.type() + " for " + info.id());
    }

    static CodecRegistry codecRegistry() {
        return registerTypeCodecs(new DefaultCodecRegistry("cutomRegistry"));
    }

    static CqlSession registerTypeCodecs(CqlSession session) {
        registerTypeCodecs((MutableCodecRegistry) session.getContext().getCodecRegistry());
        return session;
    }

    static MutableCodecRegistry registerTypeCodecs(MutableCodecRegistry registry) {
        registry.register(new EnumNameCodec<>(Country.class));
        registry.register(new EnumNameCodec<>(EmailType.class));
        registry.register(new EnumNameCodec<>(Language.class));
        registry.register(new EnumNameCodec<>(Timezone.class));
        registry.register(new EnumNameCodec<>(Company.class));
        return registry;
    }

    @Test
    void simpleCassandraSchema() {
        FieldModel model = SampleModels.wrapper();

        CreateTable createTable = SchemaBuilder.createTable("fields_model")
                .withPartitionKey("snapshot_id", DataTypes.TIMEUUID)
                .withClusteringColumn(LOGIN.name(), DataTypes.TEXT);

        for (FieldInfo info : model.getFieldInfos()) {
            createTable = createTable.withColumn(info.id().code(), cqlType(info));
        }

        CreateTableWithOptions createTableWithOptions = createTable.withClusteringOrder(LOGIN.name(),
                ClusteringOrder.DESC);
        System.out.println(createTableWithOptions.asCql());
    }

    @Test
    void simpleCasandraInsert() {
        FieldModel model = SampleModels.wrapper();
        CodecRegistry codecRegistry = codecRegistry();
        Map<String, Term> values = model.stream().collect(Collectors.toMap(
                e -> e.getKey().code(),
                e -> QueryBuilder.literal(e.getValue(), codecRegistry)));
        Insert insertRequest = QueryBuilder.insertInto("fields_model")
                .value("snapshot_id", QueryBuilder.literal(Uuids.timeBased()))
                .values(values);
        System.out.println(insertRequest.asCql());
    }

}
