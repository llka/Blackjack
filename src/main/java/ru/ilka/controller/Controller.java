package ru.ilka.controller;

import com.mysql.jdbc.ConnectionImpl;
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
import java.util.Enumeration;
import java.util.Timer;
import java.lang.reflect.Field;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;


import static ru.ilka.controller.ControllerConstants.CANCEL_TIMER_FIELD;

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
                logger.info("closing driver " + driver);
            }
        } catch (SQLException e) {
            logger.error(" DriverManager wasn't found." + e);
        }

        /*  PermGen memory leaks occur in web containers when a webapp is unloaded, however some GC roots still
            refer to classes loaded by the that webapp's ClassLoader. This prevents the ClassLoader from being
            garbage collected, and hence all the class definitions remain in memory.

            The statement cancellation timer is implemented as a static attribute of type java.util.
            Timer within the ConnectionImpl class. When this class gets loaded the timer is initialized via a static
            initialization block. Behind the scenes the JVM starts a thread to service timer tasks.

            When you unload your webapp, the cancellation timer thread does not terminate.
            This means that the ClassLoader cannot be collected, resulting in a PermGen memory leak.
            https://bugs.mysql.com/bug.php?id=36565 */

        if (ConnectionImpl.class.getClassLoader() == getClass().getClassLoader()) {
            try {
                Field f = ConnectionImpl.class.getDeclaredField(CANCEL_TIMER_FIELD);
                f.setAccessible(true);
                Timer timer = (Timer) f.get(null);
                timer.cancel();
            } catch (NoSuchFieldException  | IllegalAccessException e) {
                logger.error("Error in MySQL Statement Cancellation Timer closing" + e);
            }
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
