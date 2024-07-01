package com.tictactoe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "LogicServlet", value = "/logic")
public class LogicServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(LogicServlet.class);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        LOGGER.info("LogicServlet doGet");
        HttpSession currentSession = req.getSession();
        Field currentField = extractField(currentSession);

        int index = getSelectedIndex(req);
        Sign currentSign = currentField.getField().get(index);

        if (Sign.EMPTY != currentSign) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            try {
                dispatcher.forward(req, resp);
            }catch (ServletException | IOException e) {
                LOGGER.error("An exception occurred while forwarding to /index.jsp", e);
            }
            return;
        }
        currentField.getField().put(index, Sign.CROSS);
        LOGGER.debug("Field updated successfully: {}", currentField.getField());
        try {
            if (checkWin(resp, currentSession, currentField)) {
                return;
            }
        }catch (IOException e) {
            LOGGER.error("An exception occurred while checking winner (cross)", e);
        }

        int emptyFieldIndex = currentField.getEmptyFieldIndex();
        if (emptyFieldIndex >= 0) {
            currentField.getField().put(emptyFieldIndex, Sign.NOUGHT);
            LOGGER.debug("Field updated successfully: {}", currentField.getField());
            try {
                if (checkWin(resp, currentSession, currentField)) {
                    return;
                }
            }catch (IOException e) {
                LOGGER.error("An exception occurred while checking winner (nought)", e);
            }
        }else{
            currentSession.setAttribute("draw", true);

            List<Sign> data = currentField.getFieldData();
            currentSession.setAttribute("data", data);
            try {
                resp.sendRedirect("/index.jsp");
            }catch (IOException e) {
                LOGGER.error("An exception occurred while redirecting to /index.jsp", e);
            }
            return;
        }

        List<Sign> data = currentField.getFieldData();

        currentSession.setAttribute("data", data);
        currentSession.setAttribute("field", currentField);

        try {
            resp.sendRedirect("/index.jsp");
        }catch (IOException e) {
            LOGGER.error("An exception occurred while redirecting to /index.jsp", e);
        }
    }

    private int getSelectedIndex(HttpServletRequest request) {
        LOGGER.info("Getting 'click' parameter from request");
        String click = request.getParameter("click");
        if (click != null && click.matches("\\d+")) {
            return Integer.parseInt(click);
        } else {
            LOGGER.warn("'click' parameter is not a valid number: {}", click);
            return 0;
        }
    }

    private Field extractField(HttpSession session) {
        Object field = session.getAttribute("field");
        LOGGER.info("Extracting 'field' attribute from session");
        if(Field.class != field.getClass()) {
            LOGGER.error("Session contains an invalid 'field' attribute: {}", field.getClass());
            session.invalidate();
            throw new RuntimeException("Session is broken, try one more time");
        }
        return (Field) field;
    }

    private boolean checkWin(HttpServletResponse response, HttpSession currentSession, Field field) throws IOException {
        Sign winner = field.checkWin();
        if (Sign.CROSS == winner || Sign.NOUGHT == winner) {
            currentSession.setAttribute("winner", winner);

            List<Sign> data = field.getFieldData();

            currentSession.setAttribute("data", data);

            try {
                response.sendRedirect("/index.jsp");
            }catch (IOException e){
                LOGGER.error("IOException occurred while redirecting to /index.jsp", e);
            }
            return true;
        }
        return false;
    }
}
