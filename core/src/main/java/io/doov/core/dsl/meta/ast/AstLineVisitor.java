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

import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.NaryMetadata;

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
    public void startMetadata(NaryMetadata metadata, int depth) {
        super.startMetadata(metadata, depth);
        sb.append("[");
    }

    @Override
    public void visitMetadata(NaryMetadata metadata, int depth) {
        super.visitMetadata(metadata, depth);
        sb.delete(sb.length() - 1, sb.length());
        sb.append(", ");
    }

    @Override
    protected void endMetadata(NaryMetadata metadata, int depth) {
        super.endMetadata(metadata, depth);
        sb.delete(sb.length() - 2, sb.length());
        sb.append("] ");
    }

    @Override
    protected void startMetadata(BinaryMetadata metadata, int depth) {
        super.startMetadata(metadata, depth);
        sb.append("(");
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata, int depth) {
        sb.append(bundle.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
    }

    @Override
    protected void endMetadata(BinaryMetadata metadata, int depth) {
        super.endMetadata(metadata, depth);
        sb.delete(sb.length() - 1, sb.length());
        sb.append(") ");
    }

    @Override
    protected void endMetadata(ValidationRule metadata, int depth) {
        super.endMetadata(metadata, depth);
        sb.append("\n");
    }

}
