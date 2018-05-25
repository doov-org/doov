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

import io.doov.core.FieldModel;
import io.doov.sample.field.SampleFieldId;
import io.doov.sample.model.FavoriteWebsite;
import io.doov.sample.model.SampleModel;
import io.doov.sample.wrapper.SampleModelWrapper;

public class ExampleAccount {

    public class WithJavaBean {

        public String readSomeStuff(SampleModel model) {
            if (model == null) {
                return null;
            }
            if (model.getAccount() == null) {
                return null;
            }
            if (model.getAccount().getTop3WebSite() == null) {
                return null;
            }
            if (model.getAccount().getTop3WebSite().size() < 3) {
                return null;
            }
            FavoriteWebsite website = model.getAccount().getTop3WebSite().get(2);
            return website != null ? website.getUrl() : null;
        }

        public void updateSomeStuff(SampleModel model, String url) {
            if (model == null) {
                return;
            }
            if (model.getAccount() == null) {
                return;
            }
            if (model.getAccount().getTop3WebSite() == null) {
                return;
            }
            if (model.getAccount().getTop3WebSite().size() < 3) {
                return;
            }
            FavoriteWebsite website = model.getAccount().getTop3WebSite().get(2);
            if (website != null) {
                website.setUrl(url);
            }
        }

    }

    public class WithKeyValueModel {

        public FieldModel asFielModel(SampleModel model) {
            return new SampleModelWrapper(model);
        }

        public String readSomeStuff(FieldModel fieldModel) {
            return fieldModel.get(SampleFieldId.FAVORITE_SITE_URL_2);
        }

        public void updateSomeStuff(FieldModel fieldModel, String url) {
            fieldModel.set(SampleFieldId.FAVORITE_SITE_URL_2, url);
        }

    }

}
