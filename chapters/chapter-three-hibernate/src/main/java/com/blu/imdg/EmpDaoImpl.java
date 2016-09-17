package com.blu.imdg;

import com.blu.imdg.dao.EmpDao;
import com.blu.imdg.dto.Employee;
import com.blu.imdg.dto.ExchangeRate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by shamim on 24/06/16.
 */
public class EmpDaoImpl implements EmpDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Employee> getAllEmployees() {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Employee e");
        List<Employee> employees =  query.list();
        session.close();
        if(employees != null && !employees.isEmpty()){
            return employees;
        }
        return Collections.emptyList();
    }

    public void create(Integer empno, String ename, String job, Integer mgr) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();
        Employee emp = new Employee();
        emp.setEmpno(empno);
        emp.setEname(ename);
        emp.setJob(job);
        emp.setMgr(mgr);
        emp.setSal(1111);
        emp.setDeptno(10);
        emp.setDate(new Date(System.currentTimeMillis()));
        emp.setComm(111);
        session.save(emp);
        session.getTransaction().commit();
        session.close();
    }

    public List<Employee> getEmpByName(String ename) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("from Employee e where e.ename=:ename");
        query.setParameter("ename", ename);
        query.setCacheable(true);
        List<Employee> employees =  query.list();
        session.close();
        return employees;
    }

    @Override
    @Cacheable(value = "exchangeRate")
    public String getExchangeRateByRegion(String region) {
        Session session = sessionFactory.openSession();
        // in real life, it should be current date time
        SQLQuery query = session.createSQLQuery("select * from exchangerate e where e.ratedate = TO_DATE('2015-05-02','YYYY-MM-DD') and e.region=:region");
        query.setParameter("region", region);
        query.addEntity(ExchangeRate.class);
        ExchangeRate res =  (ExchangeRate)query.uniqueResult();
        session.close();
        return String.valueOf(res.getUsdollar());
    }

    @Override
    @CacheEvict(value = "exchangeRate", key = "#e.region")
    public void updateExchange(ExchangeRate e) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        SQLQuery query =  session.createSQLQuery("update exchangerate \n" +
                " set usdollar = :usdollar" +
                " where region = :region and ratedate = TO_DATE('2015-05-02','YYYY-MM-DD')") ;

        query.setParameter("region", e.getRegion());
        query.setParameter("usdollar", e.getUsdollar());
        query.addEntity(ExchangeRate.class);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
