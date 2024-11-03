package exception;

public class CloseResultSetException extends RuntimeException {
    public CloseResultSetException(String message) {
        super(message);
    }
}