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
import org.mockito.Mockito;

import io.doov.core.FieldModel;
import io.doov.sample.field.SampleFieldId;
import io.doov.sample.model.*;

public class MockDomainModelTest {

    @Test
    public void mockJavaBean() {
        SampleModel model = new SampleModel();

        Account account = new Account();
        account.setEmail("foo@bar.com");
        model.setAccount(account);

        User user = new User();
        user.setLastName("foo");
        user.setFirstName("bar");
        model.setUser(user);

        // ... do some testing
    }

    @Test
    public void mockWithMockito() {
        /*
         * This feature will not work when any return type of methods included in the chain cannot be mocked
         * (for example: is a primitive or a final class). This is because of java type system.
         */
        SampleModel model = Mockito.mock(SampleModel.class, Mockito.RETURNS_DEEP_STUBS);

        Mockito.when(model.getAccount().getEmail()).thenReturn("foo@bar.com");
        Mockito.when(model.getUser().getLastName()).thenReturn("foo");
        Mockito.when(model.getUser().getFirstName()).thenReturn("bar");

        // ... do some testing
    }

    @Test
    public void mockWithModelMap() {
        FieldModel fieldModel = new SampleModelWrapper(new SampleModel());

        fieldModel.set(SampleFieldId.EMAIL, "foo@bar.com");
        fieldModel.set(SampleFieldId.LAST_NAME, "foo");
        fieldModel.set(SampleFieldId.FIRST_NAME, "bar");

        // ... do some testing
    }
}


