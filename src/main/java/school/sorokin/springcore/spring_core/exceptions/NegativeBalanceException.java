package school.sorokin.springcore.spring_core.exceptions;

public class NegativeBalanceException extends Exception{
    public NegativeBalanceException(String message) {
        super(message);
    }
}
