package ru.ilka.command.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.entity.GameSettings;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.LogicException;
import ru.ilka.logic.SettingsLogic;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ru.ilka.controller.ControllerConstants.SETTINGS_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class ChangeSettingsCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger(ChangeSettingsCommand.class);

    private static final String PARAM_MIN_BET = "minBet";
    private static final String PARAM_MAX_BET = "maxBet";
    private static final String PAGE_SETTINGS = "path.page.settings";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandException {
        String page = ConfigurationManager.getProperty(PAGE_SETTINGS);

        int minBet = Integer.parseInt(request.getParameter(PARAM_MIN_BET));
        int maxBet = Integer.parseInt(request.getParameter(PARAM_MAX_BET));
        ServletContext servletContext = request.getServletContext();
        GameSettings settings = (GameSettings) servletContext.getAttribute(SETTINGS_KEY);

        if(minBet != settings.getMinBet() || maxBet != settings.getMaxBet()){
            SettingsLogic settingsLogic = new SettingsLogic();
            try {
                settingsLogic.changeBetLimits(minBet,maxBet);
            } catch (LogicException e) {
                throw new CommandException("Can't change bet limits ", e);
            }
        }

        return page;
    }
}
