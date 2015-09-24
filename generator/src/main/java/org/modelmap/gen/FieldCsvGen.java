/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package org.modelmap.gen;

import static java.util.Arrays.asList;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.modelmap.core.FieldId;

public class FieldCsvGen {

    public static void write(File output, List<VisitorPath> collected) throws MojoExecutionException {
        try (FileWriter writter = new FileWriter(output)) {
            for (VisitorPath path : collected) {
                writter.write(path.toCsv());
            }
            for (FieldId field : fieldsWithoutPath(collected)) {
                writter.write("NO_PATH;");
                writter.write(field.toString());
                writter.write('\n');
            }
        } catch (Exception e) {
            throw new MojoExecutionException(e.getMessage(), e);
        }
    }

    private static Collection<FieldId> fieldsWithoutPath(List<VisitorPath> collected) throws Exception {
        final Set<FieldId> fields = new HashSet<>();
        for (VisitorPath path : collected) {
            final FieldId[] values = (FieldId[]) path.getFieldId().getClass().getMethod("values").invoke(null);
            fields.addAll(asList(values));
        }
        for (VisitorPath path : collected) {
            fields.remove(path.getFieldId());
        }
        return fields;
    }

}
