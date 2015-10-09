package org.modelmap.sample.util;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class LoopingRule implements TestRule {
    private final int loopNumber;

    public LoopingRule(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final long startTime = System.nanoTime();
                for (int i = 0; i < loopNumber; i++)
                    base.evaluate();
                final long elapsedTime = System.nanoTime() - startTime;
                System.out.println(description.getMethodName() + "\t" + elapsedTime / 1000 + " micros");
            }
        };
    }

}
