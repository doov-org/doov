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

public class JavaScriptTest {

    public static void main(String[] args){
        System.out.println("Start of the dOOv to javascript test.");

        ScriptEngineManager sem = new ScriptEngineManager();

        ScriptEngine engine = sem.getEngineByName("nashorn");

        ByteArrayOutputStream ops = new ByteArrayOutputStream();

        REGISTRY_DEFAULT.stream()
                .peek(rule -> {
                    try {
                        ops.write("--------------------------------\n".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .forEach(rule -> rule.accept(new AstHtmlVisitor(ops, BUNDLE, Locale.ENGLISH), 0));
        System.out.println(new String(ops.toByteArray(), Charset.forName("UTF-8")));

        try{
            engine.eval(ops.toString());
        }catch(final ScriptException se){
            throw new RuntimeException(se);
        }

        System.out.println("End of the dOOv to javascript test.");
    }

}
