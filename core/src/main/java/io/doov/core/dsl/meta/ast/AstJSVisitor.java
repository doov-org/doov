/*
 * Copyright (C) by Courtanet, All Rights Reserved.
 */
package io.doov.core.dsl.meta.ast;

import java.io.IOException;
import java.io.OutputStream;

import io.doov.core.dsl.meta.LeafMetadata;
import io.doov.core.dsl.meta.Metadata;

public class AstJSVisitor extends AbstractAstVisitor {

    protected static final String BEG_UN = "var unaryvar_";
    protected static final String BEG_BIN = "var binaryvar_";
    protected static final String BEG_NAR = "var naryvar_";
    protected static final String BEG_LF = "var leafvar_";
    protected static final String BEG_STP = "var stepvar_";

    protected final OutputStream ops;
    protected int nbLeafmd; // index count for LeafMetadata 
    protected int nbUnamd, nbBinmd, nbNarmd; // index count for respectively  UnaryMetadata, BinaryMetadata and NaryMetadata
    protected int nbStpWhen; //index count for StepWhen

    public AstJSVisitor(OutputStream ops) {
        this.ops = ops;
        this.nbBinmd = 0;
        this.nbLeafmd = 0;
        this.nbNarmd = 0;
        this.nbUnamd = 0;
        this.nbStpWhen = 0;
    }

    @Override
    public void visitMetadata(Metadata metadata, int depth) {
        write(metadata.readable());
    }

    @Override
    public void startMetadata(LeafMetadata metadata, int depth) {
        write(BEG_LF + nbLeafmd);
        nbLeafmd++;
    }

    //TODO adapt each operation for each metadata type
    public void start() {

    }

    public void visit() {

    }

    public void end() {

    }

    protected void write(String str) {
        try {
            ops.write(str.getBytes("UTF-8"));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}
