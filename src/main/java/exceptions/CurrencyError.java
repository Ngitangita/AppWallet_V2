package exceptions;

public class CurrencyError extends  RuntimeException{

    public CurrencyError (String message) {
        super(message);
    }
}
