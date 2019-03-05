package io.doov.sample.validation.js.engine;

import io.doov.core.dsl.lang.ValidationRule;
import io.doov.js.ast.AstJavascriptVisitor;
import io.doov.sample.validation.SampleRules;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;
import static io.doov.js.ast.ScriptEngineFactory.create;

public class EngineTest {

    @Test
    @Disabled
    // FIXME upgrade test since leafmetadata refactoring 
    public void exec_javascript_syntax_tree() {

        List<ValidationRule> rules = SampleRules.rules();

        String mockValue = "var configuration = { max:{email:{size:24}}, min:{age:18}};\n"
                + "\tvar account = {email:\"potato@tomato.fr\", "
                + "creation: {date : \"2012-10-10\"}, country:\"FR\", company:\"LESFURETS.COM\","
                + " phone:{number:\"+334567890120\"}, timezone:\"2014-06-01T12:00:00-04:00\"};\n"
                + "\tvar user = {id:\"notnull\", birthdate:\"1980\","
                + "first:{name:\"french\"}, last:{name:\"FRIES\"} };\n";  // creation of the mock values

        System.out.println("Evaluation of the rules :");
        System.out.println("    Mock value : ");
        System.out.println("    " + mockValue);

        ScriptEngine engine = create();
        try {
            engine.eval(mockValue); // evaluating the mock values for testing purpose
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream ops = new ByteArrayOutputStream();

        final int[] index = new int[1];                                 // index as a tab, usage in lambda expression
        final int[] counter = new int[1];
        index[0] = 0;
        counter[0] = 0;
        rules.stream().forEach(rule -> {
            ops.reset();
            try {
                index[0]++;
                System.out.println("--------------------------------\n");
                new AstJavascriptVisitor(ops, BUNDLE, Locale.ENGLISH).browse(rule.metadata(), 0);
                String request = new String(ops.toByteArray(), Charset.forName("UTF-8"));
                try {
                    if (index[0] != 14) {                                // excluding some rules for now (lambda expression)
                        Object obj = engine.eval(request);              // evaluating the current rule to test
                        ops.write(("\n Rules n°" + index[0]).getBytes(StandardCharsets.UTF_8));
                        ops.write(("\n    Starting engine checking of : " + rule.readable() + "\n")
                                .getBytes(StandardCharsets.UTF_8));
                        ops.write(("\t\t-" + obj.toString() + "-\n").getBytes(StandardCharsets.UTF_8));
                        if (obj.toString().equals("true")) {
                            counter[0]++;
                        }
                        ops.write(("    Ending engine checking.\n").getBytes(StandardCharsets.UTF_8));
                    } else {
                        ops.write(("    Skipping engine checking because of mapping issue. Rule n°" + index[0] + "\n")
                                .getBytes(StandardCharsets.UTF_8));
                    }
                } catch (final ScriptException se) {
                    throw new RuntimeException(se);
                }
                System.out.println(new String(ops.toByteArray(), Charset.forName("UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Passing " + counter[0] + " out of " + index[0] + " tests with true value.");
    }
}
