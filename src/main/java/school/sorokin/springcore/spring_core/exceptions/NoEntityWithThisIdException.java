package school.sorokin.springcore.spring_core.exceptions;

public class NoEntityWithThisIdException extends Exception{
    public NoEntityWithThisIdException(String message) {
        super(message);
    }
}
