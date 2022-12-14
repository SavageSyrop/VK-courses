package ru.vk.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Path("/")
public class StaticPage {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public InputStream getInfo() {
        return StaticPage.class.getResourceAsStream("/static/mainpage.html");
    }
}
