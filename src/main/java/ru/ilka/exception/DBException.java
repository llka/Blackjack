package ru.ilka.exception;

/**
 * Signals that operation with data base failed.
 * @since %G%
 * @version %I%
 */
public class DBException extends Exception {
    public DBException() {
    }

    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(Throwable cause) {
        super(cause);
    }
}
