package ru.ilka.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class EmptyCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(EmptyCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response){
        logger.debug("Welcome to empty command");
        return "";
    }
}
