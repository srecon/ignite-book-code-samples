package com.blu.imdg.processor;

import com.blu.imdg.dto.MnpRouting;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * User: bsha
 * Date: 07.08.2016
 * Time: 19:31
 */
public class RouteProcessor implements Processor {
    private static final long CREDIT_LIMIT = 100l;
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Process the bean MnpRouting!");
        MnpRouting mnpRouting = (MnpRouting) exchange.getIn().getBody();
        if(mnpRouting != null){
            // validate phone numbers of format "1234567890"
            if (!mnpRouting.getTelephone().matches("\\d{11}") || mnpRouting.getCredit() < CREDIT_LIMIT){
                exchange.getOut().setBody("Message doesn't pass validation");
                exchange.getOut().setHeader("key", mnpRouting.getTelephone());
            } else{
                exchange.getOut().setBody(mnpRouting.toString());
                exchange.getOut().setHeader("key", mnpRouting.getTelephone());

            }

        }
        //System.out.println(mnpRouting);
    }
}
