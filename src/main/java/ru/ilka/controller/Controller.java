package ru.ilka.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.command.ActionCommand;
import ru.ilka.command.ActionFactory;
import ru.ilka.datebase.ConnectionPool;
import ru.ilka.exception.CommandException;
import ru.ilka.exception.ConnPoolException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Here could be your advertisement +375 29 3880490
 */
@WebServlet(urlPatterns = "/controller", name = "Controller")
@MultipartConfig(
        fileSizeThreshold=1024*1024*10, 	// 10 MB
        maxFileSize=1024*1024*10,
        maxRequestSize=1024*1024*10)
public class Controller extends HttpServlet {
    static Logger logger = LogManager.getLogger(Controller.class);

    private static final long serialVersionUID = 205242440943911308L;

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        process(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        process(request, response);
    }

    @Override
    public void destroy(){
        super.destroy();
        try {
            ConnectionPool.getInstance().closePool();
        } catch (ConnPoolException e) {
            logger.error("Error while closing pool " + e);
        }
        try {
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                DriverManager.deregisterDriver(driver);
                logger.info("closing driver");
            }
        } catch (SQLException e) {
            logger.error(" DriverManager wasn't found." + e);
        }
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page = null;
        ActionFactory actionFactory = new ActionFactory();
        ActionCommand command = actionFactory.defineCommand(request);
        try {
            page = command.execute(request, response);
        }catch (CommandException e) {
            logger.error("Error in command layer " + e);
        }

        if(page != null && !page.isEmpty()) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
            dispatcher.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
