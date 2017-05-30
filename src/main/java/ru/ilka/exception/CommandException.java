package ru.ilka.exception;

/**
 * Thrown to indicate that operation in command layer failed.
 * @since %G%
 * @version %I%
 */
public class CommandException extends Exception {
    public CommandException() {
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }
}
