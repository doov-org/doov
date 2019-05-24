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
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.doov.core.dsl.meta.Metadata;

public class AstVisitorUtils {

    public static String astToString(Metadata metadata, Locale locale) {
        StringBuilder stringBuilder = new StringBuilder();
        new AstLineVisitor(stringBuilder, BUNDLE, locale).browse(metadata, 0);
        return stringBuilder.toString();
    }

    public static String astToMarkdown(Metadata metadata, Locale locale) {
        StringBuilder stringBuilder = new StringBuilder();
        new AstMarkdownRenderer(stringBuilder, BUNDLE, locale).toMarkdown(metadata);
        return stringBuilder.toString();
    }

    public static Set<Metadata> collectMetadata(Metadata root) {
        return collect(root, Stream::of).collect(Collectors.toSet());
    }

    public static <T> T collect(Metadata root, T init, BiFunction<T, Metadata, T> metadataPredicate) {
        FoldVisitor<T> visitor = new FoldVisitor<>(metadataPredicate, init);
        visitor.browse(root, 0);
        return visitor.getResult();
    }

    public static <T> Stream<T> collect(Metadata root, Function<Metadata, Stream<T>> accumulator) {
        return collect(root, Stream.of(), (Stream<T> r, Metadata m) -> Stream.concat(r, accumulator.apply(m)));
    }

    public static Stream<Metadata> collectIf(Metadata root, Predicate<Metadata> metadataPredicate) {
        return collect(root, (Metadata m) -> {
            if (metadataPredicate.test(m)) {
                return Stream.of(m);
            } else {
                return Stream.of();
            }
        });
    }

    public static String toHtml(Metadata metadata, Locale locale) {
        final ByteArrayOutputStream ops = new ByteArrayOutputStream();
        DefaultHtmlWriter htmlWriter = new DefaultHtmlWriter(locale, ops, BUNDLE);
        new AstHtmlRenderer(htmlWriter).toHtml(metadata);
        return new String(ops.toByteArray(), UTF_8);
    }

}
