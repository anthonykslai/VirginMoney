package com.mkyong.error;

public class TransactionNotFoundException extends RuntimeException {

    public TransactionNotFoundException(Long year) {
        super("Transaction year not found : " + year);
    }

}
