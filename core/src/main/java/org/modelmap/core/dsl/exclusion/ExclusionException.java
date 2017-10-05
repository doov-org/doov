package org.modelmap.core.dsl.exclusion;

import org.modelmap.core.dsl.meta.Readable;

public class ExclusionException extends RuntimeException {

    public ExclusionException(String message, Readable readable) {
        super("Reason: " + message + ", rule: " + readable.readable());
    }

}
