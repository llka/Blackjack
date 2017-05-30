package ru.ilka.exception;

/**
 * Signals that connection pool was not used appropriately.
 * @since %G%
 * @version %I%
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
