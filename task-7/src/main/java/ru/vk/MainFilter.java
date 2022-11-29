package ru.vk;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        if (servletRequest.getMethod().equals("GET")) {

        }

        switch (servletRequest.getMethod()) {
            case "GET": {
                chain.doFilter(request, response);
                return;
            }

            case "POST": {
                try {
                    String productName = servletRequest.getParameter("productName");
                    String companyName = servletRequest.getParameter("companyName");
                    Integer amount = Integer.parseInt(servletRequest.getParameter("amount"));
                    chain.doFilter(request, response);
                    return;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }

            case "OPTIONS": {
                chain.doFilter(request, response);
                return;
            }

            default: {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }

    }

    @Override
    public void destroy() {

    }
}
