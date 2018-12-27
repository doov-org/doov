/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.MetadataVisitor;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

public class AstVisitorUtils {

    public static String astToString(Metadata metadata, Locale locale) {
        StringBuilder stringBuilder = new StringBuilder();
        new AstLineVisitor(stringBuilder, BUNDLE, locale).browse(metadata, 0);
        return stringBuilder.toString();
    }

    public static String astToMarkdown(Metadata metadata, Locale locale) {
        StringBuilder stringBuilder = new StringBuilder();
        new AstMarkdownVisitor(stringBuilder, BUNDLE, locale).browse(metadata, 0);
        return stringBuilder.toString();
    }

    public static Set<Metadata> collectMetadata(Metadata root) {
        HashSet<Metadata> metadatas = new HashSet<>();
        new MetadataVisitor() {
            @Override
            public void start(Metadata metadata, int depth) {
                metadatas.add(metadata);
            }
        }.browse(root, 0);
        return metadatas;
    }

    public static void resetEvalCount(Metadata root) {
        new MetadataVisitor() {
            @Override
            public void start(Metadata metadata, int depth) {
                if (metadata instanceof PredicateMetadata) {
                    PredicateMetadata predicateMetadata = (PredicateMetadata) metadata;
                    predicateMetadata.resetEvalCount();
                }
            }
        }.browse(root, 0);
    }
}
