package ru.ilka.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.command.ActionFactory;
import ru.ilka.exception.CommandException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Here could be your advertisement +375 29 3880490
 */
@WebServlet(urlPatterns = "/Ajax", name = "AjaxController")
public class AjaxController extends HttpServlet {
    static Logger logger = LogManager.getLogger(AjaxController.class);

    private static final long serialVersionUID = 905242440943999308L;
    private static final String CONTENT_TYPE = "text/html";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        process(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionFactory actionFactory = new ActionFactory();
        ActionCommand command = actionFactory.defineCommand(request);

        try {
            response.setContentType(CONTENT_TYPE);
            PrintWriter printWriter;
            try {
                printWriter = response.getWriter();
                String result = command.execute(request,response);
                if(!result.isEmpty()) {
                    printWriter.println(result);
                }
            } catch (IOException e) {
                logger.error("Can't write new card in ajax controller " + e);
            }
        } catch (CommandException e) {
            logger.error("Error in command layer " + e);
        }
    }
}
