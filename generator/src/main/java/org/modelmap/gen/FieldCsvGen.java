package org.modelmap.gen;

import static java.util.Collections.addAll;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import org.apache.maven.plugin.MojoExecutionException;
import org.modelmap.core.FieldId;

public class FieldCsvGen {

    public static void write(File output, List<VisitorPath> collected) throws MojoExecutionException {
        try (FileWriter writter = new FileWriter(output)) {
            for (VisitorPath path : collected) {
                writter.write(toCsv(path));
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

    public static String toCsv(VisitorPath path) {
        return path.getBaseClass().getSimpleName().toLowerCase() + "." + path.displayPath() + ';'
                        + path.getFieldId() + ';'
                        + path.getGetMethod().getReturnType().getSimpleName() + '\n';
    }


    private static Collection<FieldId> fieldsWithoutPath(List<VisitorPath> collected) throws Exception {
        final Set<FieldId> fields = new HashSet<>();
        collected.forEach(path -> addAll(fields, path.getFieldId().getClass().getEnumConstants()));
        collected.forEach(path -> fields.remove(path.getFieldId()));
        return fields;
    }
}
