/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.ts.ast.writer;

import java.util.*;
import java.util.stream.Collectors;

public class ImportSpec {

    private final Map<String, String> symbols;
    private final String from;

    public static ImportSpec starImport(String as, String from) {
        return new ImportSpec("*", as, from);
    }

    public ImportSpec(String from, Map<String, String> symbolMap) {
        this.from = from;
        this.symbols = new HashMap<>(symbolMap);
    }

    public ImportSpec(String symbol, String as, String from) {
        this(from, Collections.singletonMap(symbol, as));
    }

    public ImportSpec(String symbol, String from) {
        this(symbol, null, from);
    }

    public Map<String, String> symbols() {
        return symbols;
    }

    public String from() {
        return from;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ImportSpec that = (ImportSpec) o;
        return Objects.equals(symbols, that.symbols) &&
                Objects.equals(from, that.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbols, from);
    }

    public boolean isStarImport() {
        return symbols.containsKey("*");
    }

    public String toImportStatement() {
        String importSymbol;
        if (this.isStarImport()) {
            importSymbol = symbols.entrySet().stream()
                    .map(e -> e.getKey() + " as " + e.getValue())
                    .collect(Collectors.joining(","));
        } else {
            importSymbol = symbols.entrySet().stream()
                    .map(e -> {
                        if (e.getValue() != null) {
                            return e.getKey() + " as " + e.getValue();
                        } else {
                            return e.getKey();
                        }
                    }).collect(Collectors.joining(", ", "{", "}"));
        }
        return String.format("import %s from '%s';", importSymbol, this.from);
    }
}
