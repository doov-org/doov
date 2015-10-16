/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.example;

import org.junit.Test;
import org.mockito.Mockito;
import org.modelmap.core.FieldModel;
import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.model.*;

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
        final SampleModel model = Mockito.mock(SampleModel.class, Mockito.RETURNS_DEEP_STUBS);

        Mockito.when(model.getAccount().getEmail()).thenReturn("foo@bar.com");
        Mockito.when(model.getUser().getFirstName()).thenReturn("foo");
        Mockito.when(model.getUser().getLastName()).thenReturn("bar");

        // ... do some testing
    }

    @Test
    public void mockWithModelMap() {
        FieldModel model = new SampleModelWrapper(new SampleModel());

        model.set(SampleFieldId.EMAIL, "foo@bar.com");
        model.set(SampleFieldId.LAST_NAME, "foo");
        model.set(SampleFieldId.FIRST_NAME, "bar");

        // ... do some testing
    }
}


