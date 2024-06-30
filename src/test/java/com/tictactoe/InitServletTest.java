package com.tictactoe;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import static org.mockito.Mockito.*;

class InitServletTest {
    @Test
    void testDoGet() throws ServletException, IOException {
        InitServlet initServlet = new InitServlet();

        ServletConfig config = mock(ServletConfig.class);
        ServletContext servletContext = mock(ServletContext.class);
        when(config.getServletContext()).thenReturn(servletContext);
        initServlet.init(config);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);

        when(request.getSession(true)).thenReturn(session);
        when(servletContext.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        initServlet.doGet(request, response);

        verify(session).setAttribute(eq("field"), any(Field.class));
        verify(session).setAttribute(eq("data"), anyList());
        verify(dispatcher).forward(request, response);
    }
}