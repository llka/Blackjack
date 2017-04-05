package ru.ilka.exception;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class ConnPoolException extends Exception {
    public ConnPoolException() {
    }

    public ConnPoolException(String message) {
        super(message);
    }

    public ConnPoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnPoolException(Throwable cause) {
        super(cause);
    }
}
