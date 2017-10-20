/*
 * Copyright 2017 Courtanet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
package io.doov.gen;

import static java.util.Collections.addAll;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

import io.doov.core.FieldId;

class FieldCsvGen {

    public static void write(File output, Map<FieldId, VisitorPath> fieldPaths) {
        try (FileWriter writter = new FileWriter(output)) {
            writter.write("Path;Id;Type\n");
            for (VisitorPath path : fieldPaths.values()) {
                writter.write(toCsv(path));
            }
            for (FieldId field : fieldsWithoutPath(fieldPaths.values())) {
                writter.write("NO_PATH;");
                writter.write(field.toString());
                writter.write('\n');
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static String toCsv(VisitorPath path) {
        return path.getBaseClass().getSimpleName().toLowerCase() + "." + path.displayPath() + ';'
                        + path.getFieldId() + ';'
                        + path.getGetMethod().getReturnType().getSimpleName() + '\n';
    }

    private static Collection<FieldId> fieldsWithoutPath(Collection<VisitorPath> collected) {
        final Set<FieldId> fields = new HashSet<>();
        collected.forEach(path -> addAll(fields, path.getFieldId().getClass().getEnumConstants()));
        collected.forEach(path -> fields.remove(path.getFieldId()));
        return fields;
    }
}
