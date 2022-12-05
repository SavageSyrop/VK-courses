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
                    String productName = servletRequest.getParameter("name");
                    String companyName = servletRequest.getParameter("company_name");
                    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                    Integer amount = 0;

                    if (productName == null || companyName == null) {
                        httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Empty productName pr companyName parameters");
                        return;
                    }

                    try {
                        amount = Integer.parseInt(servletRequest.getParameter("amount"));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        httpServletResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Non numeric amount");
                        return;
                    }

                    chain.doFilter(request, response);
                    return;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
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
