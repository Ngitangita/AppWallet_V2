package exceptions;

public class AccountError extends RuntimeException {

    public AccountError (String message) {
        super(message);
    }
}
