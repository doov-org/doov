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

import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.NaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.PredicateMetadata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import static io.doov.core.dsl.meta.DefaultOperator.count;
import static io.doov.core.dsl.meta.DefaultOperator.sum;
import static io.doov.core.dsl.meta.MetadataType.BINARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static java.util.stream.Collectors.toList;

public class AstLinePercentVisitor extends AstLineVisitor {

    public AstLinePercentVisitor(StringBuilder stringBuilder, ResourceProvider bundle, Locale locale) {
        super(stringBuilder, bundle, locale);
    }

    @Override
    public void startWhen(Metadata metadata, int depth) {
        sb.append(percentage((PredicateMetadata) metadata.children().findFirst().orElse(null)) + " ");
        super.startWhen(metadata, depth);
    }

    @Override
    public void startBinary(BinaryPredicateMetadata metadata, int depth) {
        super.startBinary(metadata, depth);
        if (metadata.children().collect(toList()).get(0).type() == NARY_PREDICATE) {
            sb.append(percentage(metadata));
        }
    }

    @Override
    public void startNary(NaryPredicateMetadata metadata, int depth) {
        if (metadata.getOperator() != count && metadata.getOperator() != sum) {
            sb.append(percentage(metadata));
        }
        sb.append(formatCurrentIndent());
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
        sb.append("[");
    }

    @Override
    protected String formatLeafMetadata(LeafMetadata<?> metadata) {
        if (stackPeekType() == BINARY_PREDICATE) {
            return super.formatLeafMetadata(metadata);
        }
        return (metadata instanceof PredicateMetadata) ? percentage((PredicateMetadata) metadata) : ""
                + super.formatLeafMetadata(metadata);
    }

    private String percentage(PredicateMetadata metadata) {
        int t = metadata.trueEvalCount();
        int f = metadata.falseEvalCount();

        if (f == 0 && t == 0) {
            return "[n/a]";
        } else {
            BigDecimal percentage = BigDecimal.valueOf(getPercentage(t, f)).setScale(1, RoundingMode.HALF_UP);
            return "[" + percentage.toPlainString() + "]";
        }
    }

    protected double getPercentage(int t, int f) {
        return t / ((double) t + f) * 100;
    }
}
