package com.blu.imdg.example9;

/**
 * Created by shamim on 01/01/17.
 */
import com.blu.imdg.common.CommonConstants;
import com.blu.imdg.common.bankData.BankDataGenerator;
import com.sun.net.httpserver.HttpServer;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteServices;
import org.apache.ignite.Ignition;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.math.BigDecimal;
import java.net.URI;
/**
 * http://localhost:9988/service/withdrawlimit?accountnum=0000*1111&amount=100
 * */

@Path("/service")
public class ServiceHttpClient {
    private static BankService bankService;
    @GET
    @Path("/withdrawlimit")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean acceptResponse (
            @QueryParam("accountnum") String accnum,
            @QueryParam("amount") int amount
    ) throws Exception
    {
        System.out.println("account number=" + accnum + " amount=" + amount);

        return bankService.validateOperation(accnum, new BigDecimal(amount));
    }

    public static void main(String[] args) {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(9988).build();
        // start the Ignite client
        Ignite ignite = Ignition.start(CommonConstants.CLIENT_CONFIG);
        IgniteServices services = ignite.services().withAsync();

        bankService = services.serviceProxy(BankService.NAME, BankService.class, /*not-sticky*/false);

        ResourceConfig config = new ResourceConfig(ServiceHttpClient.class);
        HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
    }
}
