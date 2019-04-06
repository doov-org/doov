/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.assertions;

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
