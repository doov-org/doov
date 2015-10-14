/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap;

import java.time.LocalDate;

import org.modelmap.core.FieldModel;
import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.model.SampleModel;

@SuppressWarnings("unused")
public class ExampleUser {

    public class WithJavaBean {

        public LocalDate readSomeStuff(SampleModel model) {
            if (model == null) {
                return null;
            }
            if (model.getUser() == null) {
                return null;
            }
            return model.getUser().getBirthDate();
        }

        public void updateSomeStuff(SampleModel model, LocalDate birthDate) {
            if (model == null) {
                return;
            }
            if (model.getUser() == null) {
                return;
            }
            model.getUser().setBirthDate(birthDate);
        }

    }

    public class WithKeyValueModel {

        public LocalDate readSomeStuff(FieldModel model) {
            return model.get(SampleFieldId.BIRTHDATE);
        }

        public void updateSomeStuff(FieldModel model, LocalDate birthDate) {
            model.set(SampleFieldId.BIRTHDATE, birthDate);
        }

    }
}
