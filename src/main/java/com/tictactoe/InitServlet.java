package com.tictactoe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "InitServlet", value = "/start")
public class InitServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(InitServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Handling GET request for /start");
        HttpSession currentSession = req.getSession(true); //існує - поверне поточну сесію; не існує - створить нову

        Field field = new Field();
        List<Sign> data = field.getFieldData();

        currentSession.setAttribute("field", field);
        currentSession.setAttribute("data", data);

        logger.info("Forwarding request to /index.jsp");
        try {
            getServletContext().getRequestDispatcher("/index.jsp").forward(req, resp);
            //запит і відповідь будуть надсилатися на index.jsp
        }catch (ServletException | IOException e) {
            logger.error("An exception occurred while forwarding request to /index.jsp", e);
            throw e;
        }
    }
}