package ru.ilka.command;

import ru.ilka.exception.CommandException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Here could be your advertisement +375 29 3880490
 */
public interface ActionCommand {
    String execute(HttpServletRequest request, HttpServletResponse response)
            throws CommandException;
}
