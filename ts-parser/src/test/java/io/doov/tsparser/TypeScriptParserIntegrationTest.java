package io.doov.tsparser;

import java.io.InputStream;

import org.junit.jupiter.api.Test;

import io.doov.tsparser.TypeScriptParser.SourceFileContext;
import io.doov.tsparser.testutil.TsParserTestUtil;

/**
 * Test real source files, where all the grammar rules must work together.
 */
public class TypeScriptParserIntegrationTest {
    @Test
    public void testAngularComponent() {
        InputStream testData = TypeScriptParserIntegrationTest.class
                        .getResourceAsStream("/parser/examples/app.component.ts.test");

        SourceFileContext parsed = TsParserTestUtil.test(testData, TypeScriptParser::sourceFile);

        // TsDebugHelper.printTsTree(parsed);
    }

    @Test
    public void testAngularModule() {
        InputStream testData = TypeScriptParserIntegrationTest.class
                        .getResourceAsStream("/parser/examples/app.module.ts.test");

        SourceFileContext parsed = TsParserTestUtil.test(testData, TypeScriptParser::sourceFile);

        // TsDebugHelper.printTsTree(parsed);
    }

    @Test
    public void testAngularServiceHierarchy() {
        InputStream testData = TypeScriptParserIntegrationTest.class
                        .getResourceAsStream("/parser/examples/test-service1.service.ts.test");

        SourceFileContext parsed = TsParserTestUtil.test(testData, TypeScriptParser::sourceFile);

        // TsDebugHelper.printTsTree(parsed);
    }
}
