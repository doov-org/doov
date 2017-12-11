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
