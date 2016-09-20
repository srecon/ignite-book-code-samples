package com.blu.imdg;

import com.blu.imdg.ws.WebService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.xml.ws.Endpoint;

/**
 * Created by shamim on 24/06/16.
 */
public class RunWebService {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-core.xml");
        WebService service = (WebService) ctx.getBean("serviceBean");

        Endpoint.publish("http://localhost:7001/invokeRules", service);
        System.out.println("Server start in Port .. 7001");
    }
}