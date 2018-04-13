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
package io.doov.sample.model;

import io.doov.sample.field.SampleFieldId;
import io.doov.sample.field.SamplePath;

public class FavoriteWebsite {

    public static FavoriteWebsite webSite(String name, String url) {
        FavoriteWebsite website = new FavoriteWebsite();
        website.setName(name);
        website.setUrl(url);
        return website;
    }

    @SamplePath(field = SampleFieldId.FAVORITE_SITE_NAME_1, readable = "favorite.site.name.1")
    @SamplePath(field = SampleFieldId.FAVORITE_SITE_NAME_2, readable = "favorite.site.name.2")
    @SamplePath(field = SampleFieldId.FAVORITE_SITE_NAME_3, readable = "favorite.site.name.3")
    private String name;

    @SamplePath(field = SampleFieldId.FAVORITE_SITE_URL_1, readable = "favorite.site.url.1")
    @SamplePath(field = SampleFieldId.FAVORITE_SITE_URL_2, readable = "favorite.site.url.2")
    @SamplePath(field = SampleFieldId.FAVORITE_SITE_URL_3, readable = "favorite.site.url.3")
    private String url;

    public FavoriteWebsite() {
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
