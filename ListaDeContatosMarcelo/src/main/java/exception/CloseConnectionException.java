package exception;

public class CloseConnectionException extends RuntimeException {
    public CloseConnectionException(String s) {
        super(s);
    }
}