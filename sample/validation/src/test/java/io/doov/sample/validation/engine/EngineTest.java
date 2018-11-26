package io.doov.sample.validation.engine;

import io.doov.core.dsl.meta.ast.AstJavascriptVisitor;
import io.doov.sample.validation.SampleRules;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;
import static io.doov.core.dsl.impl.DefaultRuleRegistry.REGISTRY_DEFAULT;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;

public class EngineTest {
    @BeforeAll
    public static void init() {
        new SampleRules();
    }

    @Test
    public void print_javascript_syntax_tree() {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        ScriptEngineManager sem = new ScriptEngineManager();            // creation of an engine manager
        ScriptEngine engine = sem.getEngineByName("nashorn");        // engine creation based on nashorn
        final int[] index = new int[1];                                 // index as a tab, usage in lambda expression
        index[0]=0;
        String mockValue = "var configuration = { max:{email:{size:24}}, min:{age:18}};\n"
                +"\tvar account = {email:\"potato@tomato.fr\", " +
                "creation: {date : \"2012-10-10\"}, country:\"FR\","
                +" phone:{number:\"+334567890120\"}};\n"
                +"\tvar user = {id:\"notnull\", birthdate:\"1993-08-09\"," +
                "first:{name:\"french\"}, last:{name:\"FRIES\"} };\n";  // creation of the mock values

        System.out.println("Evaluation of the rules :");
        System.out.println("    Mock value : ");
        System.out.println("    "+mockValue);
        try{

            InputStream stream = this.getClass().getResourceAsStream("/META-INF/resources/webjars/momentjs/2.10.3/min/moment.min.js");
            InputStreamReader momentJS = new InputStreamReader(stream);
            engine.eval(momentJS);              // evaluating moment.js
            engine.eval(mockValue);             // evaluating the mock values for testing purpose
        }catch(ScriptException se){
            se.printStackTrace();
        }
        REGISTRY_DEFAULT.stream().forEach(rule -> {
            ops.reset();
            try {
                index[0]++;
                System.out.println("--------------------------------\n");
                rule.accept(new AstJavascriptVisitor(ops, BUNDLE, Locale.ENGLISH), 0);
                String request = new String(ops.toByteArray(), Charset.forName("UTF-8"));
                try {
                    if(index[0]!=8 && index[0]!=14 && index[0]!=15 && index[0]!=16) { // excluding some rules for now (lambda expression)
                        Object obj = engine.eval(request);                            // evaluating the current rule to test
                        ops.write(("\n Rules n°"+index[0]).getBytes("UTF-8"));
                        ops.write(("\n    Starting engine checking of : "+ rule.readable() +"\n").getBytes("UTF-8"));
                        ops.write(("\t\t-"+obj.toString()+"-\n").getBytes("UTF-8"));
                        ops.write(("    Ending engine checking.\n").getBytes("UTF-8"));
                    }else{
                        ops.write(("    Passing engine checking because of mapping issue. Rule n°"+index[0]+"\n").getBytes("UTF-8"));
                    }
                } catch (final ScriptException se) {
                    throw new RuntimeException(se);
                }
                System.out.println(new String(ops.toByteArray(), Charset.forName("UTF-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
}
