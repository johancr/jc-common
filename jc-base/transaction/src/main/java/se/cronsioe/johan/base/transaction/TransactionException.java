package se.cronsioe.johan.base.transaction;

public class TransactionException extends RuntimeException {

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Exception ex) {
        super(message, ex);
    }
}
