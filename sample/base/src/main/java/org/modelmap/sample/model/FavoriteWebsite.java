package org.modelmap.sample.model;

import org.modelmap.sample.field.SampleFieldId;
import org.modelmap.sample.field.SamplePath;

public class FavoriteWebsite {

    @SamplePath(field = SampleFieldId.FAVORITE_SITE_NAME_1)
    @SamplePath(field = SampleFieldId.FAVORITE_SITE_NAME_2)
    @SamplePath(field = SampleFieldId.FAVORITE_SITE_NAME_3)
    private String name;


    @SamplePath(field = SampleFieldId.FAVORITE_SITE_URL_1)
    @SamplePath(field = SampleFieldId.FAVORITE_SITE_URL_2)
    @SamplePath(field = SampleFieldId.FAVORITE_SITE_URL_3)
    private String url;

    FavoriteWebsite() {
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
