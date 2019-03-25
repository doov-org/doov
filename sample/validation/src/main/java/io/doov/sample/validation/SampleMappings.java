/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.sample.validation;

import static io.doov.core.dsl.DOOV.map;
import static io.doov.core.dsl.DOOV.mappings;
import static io.doov.core.dsl.DOOV.when;
import static io.doov.core.dsl.template.ParameterTypes.$String;
import static io.doov.sample.field.dsl.DslSampleModel.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.field.types.StringFieldInfo;
import io.doov.core.dsl.lang.MappingRule;
import io.doov.core.dsl.template.TemplateMapping.Map2;
import io.doov.core.dsl.template.TemplateMapping.Registry2;

public class SampleMappings {

    public static final MappingRule DEFAULT_CONFIG_AGE      = map(18).to(configurationMinAge);
    public static final MappingRule DEFAULT_CONFIG_CAMPAIGN = map(true).to(configurationMailingCampaign);
    public static final MappingRule DEFAULT_CREATION_DATE   = map(LocalDate.now()).to(accountCreationDate);

    public static final MappingRule DEFAULT_MAPPINGS = DOOV.mappings(
            DEFAULT_CONFIG_AGE,
            DEFAULT_CONFIG_CAMPAIGN,
            DEFAULT_CREATION_DATE
    );

    public static final MappingRule DEFAULT_CONDITIONAL =
            when(SampleRules.RULE_USER_ADULT.getStepWhen().stepCondition())
                    .then(DEFAULT_CONFIG_CAMPAIGN)
                    .otherwise(map(false).to(configurationMailingCampaign));

    public static final Map2<StringFieldInfo, StringFieldInfo> TEMPLATE_MAP_GOOGLE =
            DOOV.template($String,$String).mapping(
                    (site,url) -> when(site.eq("Google")).then(
                            map("www.gougeule.com").to(url)
                    ));

    public static final Map2<StringFieldInfo, StringFieldInfo> TEMPLATE_MAP_BING =
            DOOV.template($String,$String).mapping(
                    (site,url) -> when(site.eq("bing")).then(
                            map("www.bingue.com").to(url)
                    ));

    public static final Map2<StringFieldInfo, StringFieldInfo> TEMPLATE_MAP_YAHOO =
            DOOV.template($String,$String).mapping(
                    (site,url) -> when(site.eq("Yahoo")).then(
                            map("www.yahou.com").to(url)
                    ));

    public static final Registry2<StringFieldInfo, StringFieldInfo> SITE_RESOLUTION_TEMPLATE =
            DOOV.template($String,$String).mappings((site,url) -> mappings(
                    TEMPLATE_MAP_GOOGLE.bind(site,url),
                    TEMPLATE_MAP_BING.bind(site,url),
                    TEMPLATE_MAP_YAHOO.bind(site,url)
            ));

    public static final List<MappingRule> rules = Arrays.asList(
            DEFAULT_CONFIG_AGE,
            DEFAULT_CONFIG_CAMPAIGN,
            DEFAULT_CREATION_DATE,
            DEFAULT_MAPPINGS,
            DEFAULT_CONDITIONAL,
            TEMPLATE_MAP_GOOGLE.bind(favoriteSiteName1,favoriteSiteUrl1),
            TEMPLATE_MAP_BING.bind(favoriteSiteName2,favoriteSiteUrl2),
            TEMPLATE_MAP_YAHOO.bind(favoriteSiteName3,favoriteSiteUrl3),
            SITE_RESOLUTION_TEMPLATE.bind(favoriteSiteName3,favoriteSiteUrl3)
    );

    public static void main(String[] args) throws IOException {
        SampleWriter.of("mapping_rules.html").write(rules);
        System.exit(1);
    }

}
