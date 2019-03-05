package io.doov.sample.validation.js.ast;

import io.doov.js.ast.AstJavascriptVisitor;
import io.doov.sample.validation.SampleRules;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;

public class JsVisitorTest {

    @Test
    public void print_javascript_syntax_tree() {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        SampleRules.rules().stream()
                .peek(rule -> {
                    try {
                        ops.write("--------------------------------\n".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .forEach(rule -> new AstJavascriptVisitor(ops, BUNDLE, Locale.ENGLISH).browse(rule.metadata(), 0));
        System.out.println(new String(ops.toByteArray(), Charset.forName("UTF-8")));
    }

}
