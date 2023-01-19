package com.mkyong.error;

import java.util.Set;

public class TransactionUnSupportedFieldPatchException extends RuntimeException {

    public TransactionUnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }

}
