package ru.vk.rest;


import com.google.inject.Inject;
import generatedEntities.tables.pojos.Company;
import generatedEntities.tables.pojos.Product;
import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import ru.vk.CompanyDAO;
import ru.vk.ProductDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/products")
public class ProductsREST {

    private final ProductDAO productDAO;

    private final CompanyDAO companyDAO;

    @Inject
    public ProductsREST(ProductDAO productDAO, CompanyDAO companyDAO) {
        this.productDAO = productDAO;
        this.companyDAO = companyDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        return Response.ok(productDAO.getAll()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addProduct(@NotNull Product product) {
        String responseString = "";
        try {
            companyDAO.getByName(product.getCompanyName());
        } catch (IllegalStateException e) {
            Company company = new Company(null, product.getCompanyName());
            companyDAO.create(company);
            responseString += "Company added. \n";
        }
        productDAO.create(product);
        responseString += "Product added.";
        return Response.ok(responseString).build();
    }

    @Path("/delete")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteProductsByName(@QueryParam("productName") String productName) {
        try {
            productDAO.deleteProductsByName(productName);
        } catch (IllegalStateException e) {
            return Response.status(HttpStatus.NOT_FOUND_404).build();
        }
        return Response.ok("Products deleted.").build();
    }

    @Path("/{companyName}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByCompanyName(@PathParam("companyName") String companyName) {
        try {
            companyDAO.getByName(companyName);
        } catch (IllegalStateException e) {
            return Response.status(HttpStatus.NOT_FOUND_404).build();
        }
        return Response.ok(productDAO.getAllProductsByCompanyName(companyName)).build();
    }
}
