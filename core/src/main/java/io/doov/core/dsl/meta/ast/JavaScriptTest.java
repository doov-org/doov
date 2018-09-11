/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import static io.doov.core.dsl.impl.DefaultRuleRegistry.REGISTRY_DEFAULT;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import javax.script.*;

import io.doov.core.dsl.DOOV;
import io.doov.core.dsl.lang.ValidationRule;
import io.doov.core.dsl.meta.Metadata;

public class JavaScriptTest {

    public static void main(String[] args){


        System.out.println("Start of the dOOv to javascript test.");

        ScriptEngineManager sem = new ScriptEngineManager();

        ScriptEngine engine = sem.getEngineByName("nashorn");

        ByteArrayOutputStream ops = new ByteArrayOutputStream();

        System.out.println("    Starting string building.");

        REGISTRY_DEFAULT.stream()
                .peek(rule -> {
                    try {
                        ops.write("--------------------------------\n".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .forEach(rule -> rule.accept(new AstProceduralJSVisitor(ops, BUNDLE, Locale.ENGLISH), 0));
        String request = new String(ops.toByteArray(), Charset.forName("UTF-8"));

        System.out.println(request);

        System.out.println("    Ending string building.");

        try{
            System.out.println("    Starting engine checking.");

            engine.eval(request);

            System.out.println("    Ending engine checking.");
        }catch(final ScriptException se){
            throw new RuntimeException(se);
        }

        System.out.println("End of the dOOv to javascript test.");
    }

}
