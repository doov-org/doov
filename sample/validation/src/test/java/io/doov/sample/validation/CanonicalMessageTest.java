/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import static io.doov.core.dsl.meta.DefaultOperator.match_all;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.core.dsl.lang.Context;
import io.doov.core.dsl.lang.Result;
import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.DefaultOperator;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.NaryMetadata;
import io.doov.sample.model.SampleModel;
import io.doov.sample.model.SampleModelWrapper;
import io.doov.sample.model.SampleModels;

public class CanonicalMessageTest {
    private SampleModel sampleModel;
    private FieldModel model;

    @BeforeAll
    public static void beforeAll() {
        Rules.init();
    }

    @BeforeEach
    public void before() {
        model = new SampleModelWrapper(sampleModel = SampleModels.sample());
    }

    @Test
    public void testMe() {
        sampleModel.getUser().setBirthDate(LocalDate.now().minusYears(17));
        final Result result = Rules.RULE_ACCOUNT.withShortCircuit(false).executeOn(model);
        browseMetaData(result.getContext(), result.getContext().getRootMetadata());
    }

    private void browseMetaData(Context context, Metadata metadata) {
        if (context.getValidated().contains(metadata)) {
            System.out.println("[valid] " + metadata.readable());
        } else if (context.getInvalidated().contains(metadata))
            System.out.println("[invalid] " + metadata.readable());
        if (metadata.type() == BINARY_PREDICATE) {
            BinaryMetadata binaryPredicate = (BinaryMetadata) metadata;
            if (binaryPredicate.getOperator() == match_all)
                ;
        } else if (metadata.type() == NARY_PREDICATE) {
            NaryMetadata naryPredicate = (NaryMetadata) metadata;
            if (naryPredicate.getOperator() == or)
                ;
        }
        for (Metadata child : metadata.childs())
            browseMetaData(context, child);
    }
}
