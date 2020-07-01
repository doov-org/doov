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
import static io.doov.sample.field.SampleFieldId.*;
import static java.util.stream.Collectors.toMap;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Triple;

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

public class LiveCode {

    public static void main(String[] args) {
        intro();
        mixingWithMap();
        tagFiltering();
        cqlBuilders();
        modelDiff();
    }

    private static void intro() {
        SampleModel model = new SampleModel();
        model.setAccount(new Account());
        model.getAccount().setEmail("chtijug@gmail.com");
        System.out.println(model.getAccount().getEmail());

        FieldModel fieldModel = new SampleModelWrapper(model);
        System.out.println(fieldModel.<String> get(EMAIL));

        fieldModel.set(EMAIL, "lesfurets@gmail.com");
        System.out.println(fieldModel.<String> get(EMAIL));
    }

    private static void mixingWithMap() {
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

    private static void tagFiltering() {
        FieldModel model = SampleModels.wrapper();

        Map<FieldId, Object> map = model.stream().collect(toMap(Entry::getKey, Entry::getValue));
        SampleModelWrapper newModel = map.entrySet().stream()
                        .filter(e -> e.getKey().hasTag(SampleTag.ACCOUNT))
                        // .filter(e -> e.getKey().hasTag(SampleTag.USER))
                        .collect(SampleModelWrapper.toFieldModel());

        newModel.stream().forEach(System.out::println);
    }

    private static void cqlBuilders() {
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
        
        Map<String, Term> values = model.stream().collect(toMap(
                e -> e.getKey().code(),
                e -> QueryBuilder.literal(e.getValue(), codecRegistry())));
        Insert insertRequest = QueryBuilder.insertInto("fields_model")
                .value("snapshot_id", QueryBuilder.literal(Uuids.timeBased()))
                .values(values);
        
        System.out.println(insertRequest.asCql());
    }

    private static void modelDiff() {
        FieldModel sample_1 = SampleModels.wrapper();
        FieldModel sample_2 = SampleModels.wrapper();

        sample_1.set(FAVORITE_SITE_NAME_3, null);
        sample_1.set(FAVORITE_SITE_URL_3, null);
        sample_2.set(FAVORITE_SITE_NAME_1, "LesFurets.com");
        sample_2.set(FAVORITE_SITE_URL_1, "www.lesfurets.com");
        sample_2.set(EMAILS_PREFERENCES, Collections.emptyList());

        /* stream all key-values pair from both models */
        Stream.concat(sample_1.stream().map(buildRight), sample_2.stream().map(buildLeft))

                        /* merging key-value pair in a map */
                        .collect(Collectors.toMap(Triple::getMiddle, Function.identity(), merge))

                        /* filter to keep only key with 2 differents values */
                        .values().stream().filter(isNotSame)

                        /* print keys with differents values */
                        .forEach(System.out::println);
    }



    private static Function<Entry<FieldId, Object>, Triple<Object, FieldId, Object>> buildLeft = (entry) -> Triple
                    .of(entry.getValue(), entry.getKey(), null);

    private static Function<Entry<FieldId, Object>, Triple<Object, FieldId, Object>> buildRight = (entry) -> Triple
                    .of(null, entry.getKey(), entry.getValue());

    private static Predicate<Triple<Object, FieldId, Object>> isNotSame = (
                    triple) -> !Objects.equals(triple.getLeft(), triple.getRight());

    private static BinaryOperator<Triple<Object, FieldId, Object>> merge = (t1, t2) -> {
        Object left = t1.getLeft() != null ? t1.getLeft() : t2.getLeft();
        Object right = t2.getRight() != null ? t2.getRight() : t1.getRight();
        return Triple.of(left, t1.getMiddle(), right);
    };
}
