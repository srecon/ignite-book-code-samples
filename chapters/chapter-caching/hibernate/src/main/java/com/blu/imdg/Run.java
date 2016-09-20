package com.blu.imdg;

import com.blu.imdg.dao.EmpDao;
import com.blu.imdg.dto.Employee;
import com.blu.imdg.dto.ExchangeRate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;
import java.util.List;

/**
 * Created by shamim on 24/06/16.
 */
public class Run {
    public static void main(String[] args) {
        System.out.println("Hello Hibernate after a long time!!");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring-core.xml");
        EmpDao empDao =  (EmpDao) ctx.getBean("empDAO");
        //List<Employee> employee = empDao.getAllEmployees();
        //String res = empDao.getExchangeRateByRegion("Moscow");
        ExchangeRate e = new ExchangeRate();
        e.setRegion("Moscow");
        e.setRateDate(new Date(System.currentTimeMillis()));
        e.setUsdollar(71.17);
        empDao.updateExchange(e);
        System.out.println("return size:");
    }
}
