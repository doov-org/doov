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

import static io.doov.core.dsl.meta.DefaultOperator.and;
import static io.doov.core.dsl.meta.DefaultOperator.or;
import static io.doov.core.dsl.meta.MetadataType.EMPTY;
import static io.doov.core.dsl.meta.MetadataType.NARY_PREDICATE;
import static io.doov.core.dsl.meta.MetadataType.TEMPLATE_PARAM;

import java.util.Locale;

import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.Metadata;
import io.doov.core.dsl.meta.NaryMetadata;
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
        if ((metadata.getOperator() == and || metadata.getOperator() == or)
                || (metadata.getLeft().type() == NARY_PREDICATE)) {
            sb.append(depth > 0 ? "(" : "");
        } else if (metadata.type() == TEMPLATE_PARAM) {
            sb.append("{");
        }
    }

    @Override
    public void afterChildBinary(BinaryMetadata metadata, Metadata child, boolean hasNext, int depth) {
        if (metadata.type() == TEMPLATE_PARAM && metadata.getRight().type() != EMPTY) {
            //sb.append(bundle.get(metadata.getOperator(), locale));
        } else if (hasNext && metadata.type() != TEMPLATE_PARAM) {
            sb.append(bundle.get(metadata.getOperator(), locale));
            sb.append(formatNewLine());
        } else if (hasNext) {
            sb.append(formatNewLine());
        }
    }

    @Override
    public void endBinary(BinaryMetadata metadata, int depth) {
        super.endBinary(metadata, depth);
        sb.delete(sb.length() - 1, sb.length());
        if ((metadata.getOperator() == and || metadata.getOperator() == or)
                || (metadata.getLeft().type() == NARY_PREDICATE)) {
            sb.append(depth > 0 ? ") " : " ");
        } else if (metadata.type() == TEMPLATE_PARAM) {
            sb.append("}");
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
