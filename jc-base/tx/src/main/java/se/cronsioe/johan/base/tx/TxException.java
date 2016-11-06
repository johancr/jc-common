package se.cronsioe.johan.base.tx;

public class TxException extends RuntimeException {

    public TxException(String message) {
        super(message);
    }

    public TxException(String message, Throwable ex) {
        super(message, ex);
    }
}
