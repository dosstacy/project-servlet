package com.tictactoe;

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
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession currentSession = req.getSession();
        Field currentField = extractField(currentSession);

        int index = getSelectedIndex(req);
        Sign currentSign = currentField.getField().get(index);

        if (Sign.EMPTY != currentSign) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(req, resp);
            return;
        }
        currentField.getField().put(index, Sign.CROSS);
        if (checkWin(resp, currentSession, currentField)) {
            return;
        }

        int emptyFieldIndex = currentField.getEmptyFieldIndex();
        if (emptyFieldIndex >= 0) {
            currentField.getField().put(emptyFieldIndex, Sign.NOUGHT);
            if (checkWin(resp, currentSession, currentField)) {
                return;
            }
        }else{
            currentSession.setAttribute("draw", true);

            List<Sign> data = currentField.getFieldData();
            currentSession.setAttribute("data", data);

            resp.sendRedirect("/index.jsp");
            return;
        }

        List<Sign> data = currentField.getFieldData();

        currentSession.setAttribute("data", data);
        currentSession.setAttribute("field", currentField);

        resp.sendRedirect("/index.jsp");
    }

    private int getSelectedIndex(HttpServletRequest request) {
        String click = request.getParameter("click");
        if (click != null && click.matches("\\d+")) {
            return Integer.parseInt(click);
        } else {
            return 0;
        }
    }

    private Field extractField(HttpSession session) {
        Object field = session.getAttribute("field");
        if(Field.class != field.getClass()) {
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

            response.sendRedirect("/index.jsp");
            return true;
        }
        return false;
    }
}
