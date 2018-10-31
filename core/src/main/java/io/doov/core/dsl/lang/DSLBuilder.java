package io.doov.core.dsl.lang;

import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToMarkdown;
import static io.doov.core.dsl.meta.ast.AstVisitorUtils.astToString;

import java.util.Locale;

import io.doov.core.dsl.meta.Metadata;

public interface DSLBuilder extends Readable {

    Metadata metadata();

    /**
     * Returns the human readable version of this object.
     *
     * @param locale the locale to use
     * @return the readable string
     * @see #readable()
     */
    default String readable(Locale locale) {
        return astToString(metadata(), locale).trim();
    }

    @Override
    default String readable() {
        return readable(Locale.getDefault());
    }

    default String markdown() {
        return markdown(Locale.getDefault());
    }

    default String markdown(Locale locale) {
        return astToMarkdown(metadata(), locale);
    }
}
