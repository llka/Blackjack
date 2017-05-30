package ru.ilka.exception;

/**
 * Thrown to indicate that operation in logic layer failed.
 * @since %G%
 * @version %I%
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
