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
package io.doov.assertions.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.ListAssert;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.BulletList;
import org.commonmark.node.ListItem;
import org.commonmark.node.Node;
import org.commonmark.node.OrderedList;
import org.commonmark.node.Text;

public class MarkdownAssert extends AbstractAssert<MarkdownAssert, Node> {

    public MarkdownAssert(Node actual, Class<?> selfType) {
        super(actual, selfType);
    }

    public IntegerAssert countOrderedList() {
        final AtomicInteger count = new AtomicInteger();
        actual.accept(new AbstractVisitor() {
            @Override
            public void visit(OrderedList orderedList) {
                count.incrementAndGet();
                super.visit(orderedList);
            }
        });
        return new IntegerAssert(count.get());
    }

    public IntegerAssert countText() {
        final AtomicInteger count = new AtomicInteger();
        actual.accept(new AbstractVisitor() {

            @Override
            public void visit(Text text) {
                count.incrementAndGet();
                super.visit(text);
            }
        });
        return new IntegerAssert(count.get());
    }

    public IntegerAssert countBulletList() {
        final AtomicInteger count = new AtomicInteger();
        actual.accept(new AbstractVisitor() {
            @Override
            public void visit(BulletList bulletList) {
                count.incrementAndGet();
                super.visit(bulletList);
            }
        });
        return new IntegerAssert(count.get());
    }

    public IntegerAssert countListItem() {
        final AtomicInteger count = new AtomicInteger();
        actual.accept(new AbstractVisitor() {
            @Override
            public void visit(ListItem listItem) {
                count.incrementAndGet();
                super.visit(listItem);
            }
        });
        return new IntegerAssert(count.get());
    }

    public ListAssert<String> textNodes() {
        final List<String> collector = new ArrayList<String>();
        actual.accept(new AbstractVisitor() {

            @Override
            public void visit(Text text) {
                collector.add(text.getLiteral());
                super.visit(text);
            }
        });
        return new ListAssert<>(collector);
    }
}
