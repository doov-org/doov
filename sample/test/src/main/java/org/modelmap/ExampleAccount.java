/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap;

import org.modelmap.core.FieldModel;
import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.model.FavoriteWebsite;
import org.modelmap.sample.model.SampleModel;

@SuppressWarnings("unused")
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

        public String readSomeStuff(FieldModel model) {
            return model.get(SampleFieldId.FAVORITE_SITE_URL_2);
        }

        public void updateSomeStuff(FieldModel model, String url) {
            model.set(SampleFieldId.FAVORITE_SITE_URL_2, url);
        }

    }

}
