package com.blu.imdg.common;


import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by mzheludkov on 04.07.16.
 */
public class JSEvaluate {

    public static boolean evaluateJs(String xml, String js) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        Bindings bindings = engine.createBindings();
        try {
            bindings.put("xpath", new XPathExecutor(xml));
            return (boolean) engine.eval(js, bindings);
        } catch (final Exception err) {
            throw new RuntimeException(err);
        }
    }

}
