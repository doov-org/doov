package io.doov.sample;

import org.junit.jupiter.api.Test;

import io.doov.core.FieldModel;
import io.doov.sample.field.SampleFieldId;
import io.doov.sample.model.*;

public class LiveCodeSoftshake {

    @Test
    public void intro() {
        SampleModel model = new SampleModel();
        model.setAccount(new Account());
        model.getAccount().setEmail("jussieu@yeah.com");
        System.out.println(model.getAccount().getEmail());

        FieldModel fieldModel = new SampleModelWrapper(model);
        System.out.println(fieldModel.<String> get(SampleFieldId.EMAIL));

        fieldModel.set(SampleFieldId.EMAIL, "lesfurets@gmail.com");
        System.out.println(fieldModel.<String> get(SampleFieldId.EMAIL));
    }

}
