/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import static org.modelmap.sample.field.SampleFieldIdInfo.accountId;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountPreferencesMail;
import static org.modelmap.sample.field.SampleFieldIdInfo.accountTimezone;
import static org.modelmap.sample.field.SampleFieldIdInfo.userBirthdate;
import static org.modelmap.sample.model.EmailType.ADMINISTRATOR;
import static org.modelmap.sample.model.EmailType.PRIVATE;
import static org.modelmap.sample.model.Timezone.ETC_GMT;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import org.junit.Test;
import org.modelmap.core.FieldModel;
import org.modelmap.core.dsl.DSL;
import org.modelmap.core.dsl.lang.RuleRegistry;
import org.modelmap.core.dsl.lang.ValidationRule;
import org.modelmap.sample.model.SampleModels;

public class DSLRegistryTest {

    private FieldModel model = SampleModels.wrapper();

    private enum Registry implements RuleRegistry {
        DEFAULT;

        private List<ValidationRule> rules = new ArrayList<>();

        @Override
        public void register(ValidationRule rule) {
            rules.add(rule);
        }

        @Override
        public Stream<ValidationRule> stream() {
            return rules.stream();
        }
    }

    @Test
    public void sample1() {
        DSL.when(accountId().eq(1L)).validate().withMessage("incorrect account id").registerOn(Registry.DEFAULT);
        DSL.when(accountId().eq(1L)).validate().registerOn(Registry.DEFAULT);
        DSL.when(accountId().eq(1L).not()).validate().withMessage("incorrect account id").registerOn(Registry.DEFAULT);
        DSL.when(userBirthdate().eq(LocalDate.of(1980, 8, 1))).validate()
                        .withMessage("valid birthdate is August 1, 1980").registerOn(Registry.DEFAULT);
        DSL.when(userBirthdate().between(LocalDate.of(1980, 1, 1), LocalDate.of(1980, 12, 31)))
                        .validate().withMessage("valid birthdate is in year 1980").registerOn(Registry.DEFAULT);
        DSL
                        .when(userBirthdate().between(LocalDate.of(1980, 1, 1), LocalDate.of(1980, 12, 31))
                                        .and(accountId().notEq(9L)).or((accountTimezone()).eq(ETC_GMT)))
                        .validate()
                        .withMessage("valid birthdate is in year 1980, " +
                                        "valid ID is different than 9, and " +
                                        "valid timezone is ETC_GMT")
                        .registerOn(Registry.DEFAULT);
        DSL.when(accountPreferencesMail().eq(EnumSet.of(ADMINISTRATOR, PRIVATE))).validate()
                        .registerOn(Registry.DEFAULT);

        Registry.DEFAULT.stream().map(r -> r.executeOn(model));
    }

}
