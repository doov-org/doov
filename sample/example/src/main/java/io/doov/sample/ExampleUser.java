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

import java.time.LocalDate;

import io.doov.core.FieldModel;
import io.doov.sample.field.SampleFieldId;
import io.doov.sample.model.SampleModel;
import io.doov.sample.model.SampleModelWrapper;

public class ExampleUser {

    public class WithJavaBean {

        public LocalDate readSomeStuff(SampleModel model) {
            if (model == null)
                return null;
            if (model.getUser() == null)
                return null;
            return model.getUser().getBirthDate();
        }

        public void updateSomeStuff(SampleModel model, LocalDate birthDate) {
            if (model == null)
                return;
            if (model.getUser() == null)
                return;
            model.getUser().setBirthDate(birthDate);
        }

    }

    public class WithKeyValueModel {

        public FieldModel asFielModel(SampleModel model) {
            return new SampleModelWrapper(model);
        }

        public LocalDate readSomeStuff(FieldModel fieldModel) {
            return fieldModel.get(SampleFieldId.BIRTHDATE);
        }

        public void updateSomeStuff(FieldModel fieldModel, LocalDate birthDate) {
            fieldModel.set(SampleFieldId.BIRTHDATE, birthDate);
        }

    }
}
