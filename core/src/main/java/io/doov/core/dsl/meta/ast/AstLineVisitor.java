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

import java.util.Locale;

import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.RuleMetadata;
import io.doov.core.dsl.meta.i18n.ResourceProvider;
import io.doov.core.dsl.meta.predicate.BinaryPredicateMetadata;
import io.doov.core.dsl.meta.predicate.NaryPredicateMetadata;

public class AstLineVisitor extends AstTextVisitor {

    public AstLineVisitor(StringBuilder stringBuilder, ResourceProvider bundle, Locale locale) {
        super(stringBuilder, bundle, locale);
    }

    @Override
    protected String formatCurrentIndent() {
        return "";
    }

    @Override
    protected String formatNewLine() {
        return " ";
    }

    @Override
    public void startNary(NaryPredicateMetadata metadata, int depth) {
        super.startNary(metadata, depth);
        sb.append("[");
    }

    @Override
    public void afterChildNary(NaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
        super.visitNary(metadata, depth);
        if (hasNext) {
            sb.delete(sb.length() - 1, sb.length());
            sb.append(", ");
        }
    }

    @Override
    public void endNary(NaryPredicateMetadata metadata, int depth) {
        super.endNary(metadata, depth);
        sb.delete(sb.length() - 1, sb.length());
        sb.append("] ");
    }

    @Override
    public void startBinary(BinaryPredicateMetadata metadata, int depth) {
        super.startBinary(metadata, depth);
        sb.append(depth > 0 ? "(" : "");
    }

    @Override
    public void afterChildBinary(BinaryPredicateMetadata metadata, Metadata child, boolean hasNext, int depth) {
        if (hasNext) {
            sb.append(bundle.get(metadata.getOperator(), locale));
            sb.append(formatNewLine());
        }
    }

    @Override
    public void endBinary(BinaryPredicateMetadata metadata, int depth) {
        super.endBinary(metadata, depth);
        sb.delete(sb.length() - 1, sb.length());
        sb.append(depth > 0 ? ") " : " ");
    }

    @Override
    public void endRule(RuleMetadata metadata, int depth) {
        super.endRule(metadata, depth);
        formatNewLine();
    }

}
