package io.doov.js.ast;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ScriptEngineFactory {

    private static final String MOMENT_JS_SRC = "/META-INF/resources/webjars/momentjs/2.10.3/min/moment.min.js";
    private static final String ENGINE_NAME = "nashorn";

    public static ScriptEngine create() {
        ScriptEngineManager sem = new ScriptEngineManager();            // creation of an engine manager
        ScriptEngine engine = sem.getEngineByName(ENGINE_NAME);         // engine creation based on nashorn
        try {
            InputStream stream = ScriptEngineFactory.class.getResourceAsStream(MOMENT_JS_SRC);
            InputStreamReader momentJS = new InputStreamReader(stream);
            engine.eval(momentJS);                                      // evaluating moment.js
        } catch (ScriptException se) {
            se.printStackTrace();
        }
        return engine;
    }

}


