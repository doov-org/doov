/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package io.doov.sample.validation.ast;

import static io.doov.core.dsl.impl.DefaultRuleRegistry.REGISTRY_DEFAULT;
import static io.doov.core.dsl.meta.i18n.ResourceBundleProvider.BUNDLE;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.script.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.doov.core.dsl.meta.ast.*;
import io.doov.sample.validation.SampleRules;

public class RulesVisitorTest {

    @BeforeAll
    public static void init() {
        new SampleRules();
    }

    @Test
    public void print_full_syntax_tree() {
        StringBuilder sb = new StringBuilder();
        REGISTRY_DEFAULT.stream()
                .peek(rule -> sb.append("--------------------------------").append("\n"))
                .forEach(rule -> rule.accept(new AstFullVisitor(sb), 0));
        System.out.println(sb.toString());
    }

    @Test
    public void print_line_syntax_tree() {
        StringBuilder sb = new StringBuilder();
        REGISTRY_DEFAULT.stream()
                .peek(rule -> sb.append("--------------------------------").append("\n"))
                .forEach(rule -> rule.accept(new AstLineVisitor(sb, BUNDLE, Locale.ENGLISH), 0));
        System.out.println(sb.toString());
    }

    @Test
    public void print_text_syntax_tree() {
        StringBuilder sb = new StringBuilder();
        REGISTRY_DEFAULT.stream()
                .peek(rule -> sb.append("--------------------------------").append("\n"))
                .forEach(rule -> rule.accept(new AstTextVisitor(sb, BUNDLE, Locale.ENGLISH), 0));
        System.out.println(sb.toString());
    }

    @Test
    public void print_markdown_syntax_tree() {
        StringBuilder sb = new StringBuilder();
        REGISTRY_DEFAULT.stream()
                .peek(rule -> sb.append("--------------------------------").append("\n"))
                .forEach(rule -> rule.accept(new AstMarkdownVisitor(sb, BUNDLE, Locale.ENGLISH), 0));
        System.out.println(sb.toString());
    }

    @Test
    public void print_html_syntax_tree() {
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
    }


    /*Problème avec moment js. si On ajoute un mois au 31 du mois de départ (par exemple mai) alors la différence
    en mois ne sera pas égale à 1*/
    @Test
    public void print_javascript_syntax_tree() {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        // TODO add moment.min.js in libraries and redirect the inputstreamreader
        // ou <script src="https://unpkg.com/moment" />

//        REGISTRY_DEFAULT.stream()
//                .peek(rule -> {
//                    try {
//                        ops.write("--------------------------------\n".getBytes());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                })
//                .forEach(rule -> rule.accept(new AstProceduralJSVisitor(ops, BUNDLE, Locale.ENGLISH), 0));
//        System.out.println(new String(ops.toByteArray(), Charset.forName("UTF-8")));

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
            InputStreamReader momentJS = new InputStreamReader(new FileInputStream(new File("/home/jru/moment.min.js")));
            engine.eval(momentJS);              // evaluating moment.js
            engine.eval(mockValue);             // evaluating the mock values for testing purpose
        }catch(ScriptException se){
            se.printStackTrace();
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
        REGISTRY_DEFAULT.stream().forEach(rule -> {
            ops.reset();
            try {
                index[0]++;
                System.out.println("--------------------------------\n");
                rule.accept(new AstProceduralJSVisitor(ops, BUNDLE, Locale.ENGLISH), 0);
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
