package com.tictactoe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class LogicServletTest {
    private LogicServlet logicServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private Field field;
    private ServletContext servletContext;

    @BeforeEach
    public void setUp() throws Exception {
        logicServlet = new LogicServlet();

        ServletConfig config = mock(ServletConfig.class);
        servletContext = mock(ServletContext.class);
        // Налаштовуємо макет ServletConfig для повернення макету ServletContext
        when(config.getServletContext()).thenReturn(servletContext);
        // Ініціалізуємо сервлет
        logicServlet.init(config);

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        field = mock(Field.class);
    }

    @Test
    void testOccupiedCell() throws ServletException, IOException {
        Map<Integer, Sign> fieldMap = new HashMap<>();
        fieldMap.put(0, Sign.CROSS);
        when(field.getField()).thenReturn(fieldMap);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("field")).thenReturn(field);
        when(request.getParameter("click")).thenReturn("0");

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(servletContext.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        logicServlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }
}
