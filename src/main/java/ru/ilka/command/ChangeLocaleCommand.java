package ru.ilka.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.Visitor;
import ru.ilka.manager.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

import static ru.ilka.controller.ControllerConstants.VISITOR_KEY;

/**
 * Here could be your advertisement +375 29 3880490
 */
public class ChangeLocaleCommand implements ActionCommand {
    static Logger logger = LogManager.getLogger();
    private static final String PARAM_LOCALE = "locale";
    private static final String LOCALE_DELIMITER = "_";
    private static final String ATTRIBUTE_PAGE_PATH = "path";
    private static final int INDEX_LANGUAGE = 0;
    private static final int INDEX_COUNTRY = 1;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getSession().getAttribute(ATTRIBUTE_PAGE_PATH).toString();
        String page = ConfigurationManager.getProperty(path);

        Visitor visitor = (Visitor) request.getSession().getAttribute(VISITOR_KEY);

        String[] chosenLocaleInfo = request.getParameter(PARAM_LOCALE).split(LOCALE_DELIMITER);
        Locale chosenLocale = new Locale(chosenLocaleInfo[INDEX_LANGUAGE], chosenLocaleInfo[INDEX_COUNTRY]);
        if (!chosenLocale.equals(visitor.getLocale())) {
            visitor.setLocale(chosenLocale);
        }
        return page;
    }
}
