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

import java.util.Locale;

import io.doov.core.dsl.meta.*;
import io.doov.core.dsl.meta.i18n.ResourceProvider;

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
    public void startNary(NaryMetadata metadata, int depth) {
        super.startNary(metadata, depth);
        sb.append("[");
    }

    @Override
    public void afterChildNary(NaryMetadata metadata, Metadata child, boolean hasNext, int depth) {
        super.visitNary(metadata, depth);
        if (hasNext) {
            sb.delete(sb.length() - 1, sb.length());
            sb.append(", ");
        }
    }

    @Override
    public void endNary(NaryMetadata metadata, int depth) {
        super.endNary(metadata, depth);
        sb.delete(sb.length() - 1, sb.length());
        sb.append("] ");
    }

    @Override
    public void startBinary(BinaryMetadata metadata, int depth) {
        super.startBinary(metadata, depth);
        if ((metadata.getOperator() == DefaultOperator.and || metadata.getOperator() == DefaultOperator.or)
                || (metadata.getLeft().type() == MetadataType.NARY_PREDICATE)) {
            sb.append(depth > 0 ? "(" : "");
        }
    }

    @Override
    public void afterChildBinary(BinaryMetadata metadata, Metadata child, boolean hasNext, int depth) {
        if (hasNext) {
            sb.append(bundle.get(metadata.getOperator(), locale));
            sb.append(formatNewLine());
        }
    }

    @Override
    public void endBinary(BinaryMetadata metadata, int depth) {
        super.endBinary(metadata, depth);
        sb.delete(sb.length() - 1, sb.length());
        if ((metadata.getOperator() == DefaultOperator.and || metadata.getOperator() == DefaultOperator.or)
                || (metadata.getLeft().type() == MetadataType.NARY_PREDICATE)) {
            sb.append(depth > 0 ? ") " : " ");
        } else {
            sb.append(" ");
        }
    }

    @Override
    public void endRule(Metadata metadata, int depth) {
        super.endRule(metadata, depth);
        formatNewLine();
    }

}
