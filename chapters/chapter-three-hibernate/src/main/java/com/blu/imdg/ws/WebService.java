package com.blu.imdg.ws;

import com.blu.imdg.dao.EmpDao;
import com.blu.imdg.dto.Employee;
import com.blu.imdg.dto.ExchangeRate;

import javax.jws.WebMethod;
import java.util.Date;
import java.util.List;

/**
 * Created by shamim on 24/06/16.
 */
@javax.jws.WebService(name = "BusinessRulesServices",
        serviceName="BusinessRulesServices",
        targetNamespace = "http://com.blu.rules/services")
public class WebService {
    private EmpDao empDao;

    @WebMethod(exclude = true)
    public void setEmpDao(EmpDao empDao) {
        this.empDao = empDao;
    }
    @WebMethod(operationName = "getAllEmploees")
    public List<Employee> getAllEmployees() {
        return empDao.getAllEmployees();
    }
    @WebMethod(operationName = "addEmployee")
    public void addEmployee(Integer empno, String ename, String job, Integer mgr ) {
        empDao.create(empno, ename, job, mgr);
    }
    @WebMethod(operationName = "getEmpByName")
    public List<Employee> getEmpByName(String ename) {
        return empDao.getEmpByName(ename);
    }
    @WebMethod(operationName = "getExchangeRateByRegion")
    public String getExchangeRateByRegion(String str){
       return empDao.getExchangeRateByRegion(str);
    }
    @WebMethod(operationName = "updateExchangeRate")
    public void updateExchangeRate(String region, Date rateDate, double usdollar, double euro, double gbp){
        ExchangeRate e = new ExchangeRate();
        e.setRegion(region);
        e.setUsdollar(usdollar);
        e.setEuro(euro);
        e.setGbp(gbp);
        e.setRateDate(rateDate);

        empDao.updateExchange(e);
    }
}

