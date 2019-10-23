/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.test;

import static java.util.stream.Collectors.toList;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.*;

public class JestTemplate {

    private static Configuration CONFIGURATION;

    static Configuration getConfiguration() {
        if (CONFIGURATION == null) {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_20);
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateLoader(new ClassTemplateLoader(JestTemplate.class, "/"));
            CONFIGURATION = configuration;
        }
        return CONFIGURATION;
    }

    public static void write(Map<String, Object> parameters, Writer writer) {
        Configuration configuration = getConfiguration();
        try {
            Template template = configuration.getTemplate("jest-test.ftl");
            template.process(parameters, writer);
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Map<String, Object> toTemplateParameters(JestTestSpec jestTestSpec) {
        HashMap<String, Object> cfg = new HashMap<>();
        cfg.put("imports", jestTestSpec.getImports());
        cfg.put("fields", jestTestSpec.getFields());
        cfg.put("beforeEachs", jestTestSpec.getBeforeEachs());
        cfg.put("testStates", jestTestSpec.getTestStates());
        cfg.put("suiteName", jestTestSpec.getTestSuiteName());
        cfg.put("tests", jestTestSpec.getTestCases());
        return cfg;
    }

    public static String writetoString(Map<String, Object> parameters) {
        try (StringWriter writer = new StringWriter()) {
            write(parameters, writer);
            writer.flush();
            return writer.toString();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public static File writeToFile(Map<String, Object> parameters, File file) {
        try(FileWriter fileWriter = new FileWriter(file)) {
            Files.createDirectories(file.getParentFile().toPath());
            write(parameters, fileWriter);
            fileWriter.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return file;
    }

}
