package com.blu.imdg.common;

import com.sun.net.httpserver.HttpServer;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by mikl on 11.07.16.
 */
@Path("/audit")
public class HttpAuditEmulator {


    @GET
    @Path("/log")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayPlainTextHello(
            @QueryParam("msgId") String msgId,
            @QueryParam("validationResult") boolean result
    ) {
        System.out.println("log message validation result msgId=" + msgId + " result=" + result);
        return "1";
    }

    public static void main(String[] args) {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
        ResourceConfig config = new ResourceConfig(HttpAuditEmulator.class);
        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
    }
}
