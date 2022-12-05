package ru.vk;

import generatedEntities.tables.records.CompanyRecord;
import generatedEntities.tables.records.ProductRecord;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainServlet extends HttpServlet {
    private final ProductDAO productDAO;

    private final CompanyDAO companyDAO;

    public MainServlet(ProductDAO productDAO, CompanyDAO companyDAO) {
        this.productDAO = productDAO;
        this.companyDAO = companyDAO;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (ServletOutputStream outputStream = resp.getOutputStream()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<!DOCTYPE html>");
            stringBuilder.append("<html lang=\"en\">");
            stringBuilder.append("<body>");
            stringBuilder.append("<h1>All products from DB</h1>");

            for (ProductRecord productRecord : productDAO.getAll()) {
                stringBuilder.append(productRecord.toString().replace("\n", "<br>\n"));
            }

            stringBuilder.append("</body>");
            stringBuilder.append("</html>");
            outputStream.write(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productName = req.getParameter("name");
        String companyName = req.getParameter("company_name");
        Integer amount = Integer.parseInt(req.getParameter("amount"));
        try {
            companyDAO.getByName(companyName);
        } catch (IllegalStateException e) {
            CompanyRecord companyRecord = new CompanyRecord();
            companyRecord.setName(companyName);
            companyDAO.create(companyRecord);
        }
        ProductRecord productRecord = new ProductRecord();
        productRecord.setName(productName);
        productRecord.setCompanyName(companyName);
        productRecord.setAmount(amount);
        productDAO.create(productRecord);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
