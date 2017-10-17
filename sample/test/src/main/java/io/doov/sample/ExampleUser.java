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
