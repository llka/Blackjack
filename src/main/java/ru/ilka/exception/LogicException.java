package ru.ilka.exception;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class LogicException extends Exception{
    public LogicException() {
    }

    public LogicException(String message) {
        super(message);
    }

    public LogicException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogicException(Throwable cause) {
        super(cause);
    }
}
