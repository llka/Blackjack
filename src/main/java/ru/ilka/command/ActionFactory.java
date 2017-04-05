package ru.ilka.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.http.HttpServletRequest;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class ActionFactory {
    static Logger logger = LogManager.getLogger(ActionFactory.class);
    private static final String COMMAND_PARAM = "command";

    public ActionCommand defineCommand(HttpServletRequest request) {

        ActionCommand currentCommand = new EmptyCommand();

        String commandAction = request.getParameter(COMMAND_PARAM);
        if (commandAction == null || commandAction.isEmpty()) {
            return currentCommand;
        }

        CommandType currentType = CommandType.valueOf(commandAction.toUpperCase());
        currentCommand = currentType.getCurrentCommand();
        logger.debug("ActionFactory currCommand = " + currentCommand);
        return currentCommand;
    }
}
