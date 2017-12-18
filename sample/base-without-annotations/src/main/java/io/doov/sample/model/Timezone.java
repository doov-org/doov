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

public enum Timezone {
    ETC_GMT_PLUS_12("GMT-12:00"), //
    ETC_GMT_PLUS_11("GMT-11:00"), //
    ETC_GMT_PLUS_10("GMT-10:00"), //
    ETC_GMT_PLUS_9("GMT-09:00"), //
    ETC_GMT_PLUS_8("GMT-08:00"), //
    ETC_GMT_PLUS_7("GMT-07:00"), //
    ETC_GMT_PLUS_6("GMT-06:00"), //
    ETC_GMT_PLUS_5("GMT-05:00"), //
    ETC_GMT_PLUS_4("GMT-04:00"), //
    ETC_GMT_PLUS_3("GMT-03:00"), //
    ETC_GMT_PLUS_2("GMT-02:00"), //
    ETC_GMT_PLUS_1("GMT-01:00"), //
    ETC_GMT("GMT+00:00"), //
    ETC_GMT_MINUS_1("GMT+01:00"), //
    ETC_GMT_MINUS_2("GMT+02:00"), //
    ETC_GMT_MINUS_3("GMT+03:00"), //
    ETC_GMT_MINUS_4("GMT+04:00"), //
    ETC_GMT_MINUS_5("GMT+05:00"), //
    ETC_GMT_MINUS_6("GMT+06:00"), //
    ETC_GMT_MINUS_7("GMT+07:00"), //
    ETC_GMT_MINUS_8("GMT+08:00"), //
    ETC_GMT_MINUS_9("GMT+09:00"), //
    ETC_GMT_MINUS_10("GMT+10:00"), //
    ETC_GMT_MINUS_11("GMT+11:00"), //
    ETC_GMT_MINUS_12("GMT+12:00"), //
    ETC_GMT_MINUS_13("GMT+13:00"), //
    ETC_GMT_MINUS_14("GMT+14:00");

    private final String description;

    Timezone(String displayName) {
        this.description = displayName;
    }

    public String getDescription() {
        return description;
    }
}
