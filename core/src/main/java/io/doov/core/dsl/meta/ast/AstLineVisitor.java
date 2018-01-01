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

import static io.doov.core.dsl.meta.i18n.DefaultResourceBundle.BUNDLE;

import java.util.Locale;

import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.BinaryMetadata;
import io.doov.core.dsl.meta.NaryMetadata;

public class AstLineVisitor extends AstTextVisitor {

    public AstLineVisitor(StringBuilder stringBuilder, Locale locale) {
        super(stringBuilder, locale);
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
    public void startMetadata(NaryMetadata metadata) {
        super.startMetadata(metadata);
        sb.append("[");
    }

    @Override
    public void visitMetadata(NaryMetadata metadata) {
        super.visitMetadata(metadata);
        sb.delete(sb.length() - 1, sb.length());
        sb.append(", ");
    }

    @Override
    protected void endMetadata(NaryMetadata metadata) {
        super.endMetadata(metadata);
        sb.delete(sb.length() - 2, sb.length());
        sb.append("] ");
    }

    @Override
    protected void startMetadata(BinaryMetadata metadata) {
        super.startMetadata(metadata);
        sb.append("(");
    }

    @Override
    public void visitMetadata(BinaryMetadata metadata) {
        sb.append(BUNDLE.get(metadata.getOperator(), locale));
        sb.append(formatNewLine());
    }

    @Override
    protected void endMetadata(BinaryMetadata metadata) {
        super.endMetadata(metadata);
        sb.delete(sb.length() - 1, sb.length());
        sb.append(") ");
    }

    @Override
    protected void endMetadata(ValidationRule metadata) {
        super.endMetadata(metadata);
        sb.append("\n");
    }

}
