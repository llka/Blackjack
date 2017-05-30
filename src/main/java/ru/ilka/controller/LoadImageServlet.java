package ru.ilka.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.ilka.entity.Account;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static ru.ilka.controller.ControllerConstants.ACCOUNT_KEY;
import static ru.ilka.controller.ControllerConstants.AVATAR_DIRECTORY_STATIC;
import static ru.ilka.controller.ControllerConstants.DEFAULT_AVATAR_DIRECTORY;

/**
 * Responsible for loading showing images
 * @since %G%
 * @version %I%
 */
@WebServlet(urlPatterns = "/imageLoader", name = "LoadImageServlet")
@MultipartConfig(
        fileSizeThreshold=1024*1024*10, 	// 10 MB
        maxFileSize=1024*1024*10,
        maxRequestSize=1024*1024*10)
public class LoadImageServlet extends HttpServlet {
    static Logger logger = LogManager.getLogger(LoadImageServlet.class);

    private static final String TYPE_IMG_JPEG = "image/jpeg";
    private static final String DEFAULT_AVATAR = "default_avatar.jpg";
    private static final String FILE_DELIMITER = "\\";
    private static final String FORMAT_JPEG = "jpeg";

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        process(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response){
        response.setContentType(TYPE_IMG_JPEG);

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute(ACCOUNT_KEY);
        String imgName = account.getAvatar();

        OutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            logger.error("Can't get response output stream " + e);
        }
        File file = new File(AVATAR_DIRECTORY_STATIC + FILE_DELIMITER + imgName);
        try {
            BufferedImage img;
            if(file.exists()) {
                img = ImageIO.read(file);
            }else {
                img = ImageIO.read(new File(DEFAULT_AVATAR_DIRECTORY + DEFAULT_AVATAR));
            }
            ImageIO.setUseCache(false);
            ImageIO.write(img, FORMAT_JPEG, out);
        } catch (IOException e) {
            logger.error("Can't read img file " + e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                logger.error("IOException in output stream closing" + e);
            }
        }
    }
}
