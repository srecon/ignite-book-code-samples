package com.blu.imdg.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;

/**
 * Created by isatimur on 8/9/16.
 */
public class Employee implements Serializable {

    private static final AtomicInteger GENERATED_ID = new AtomicInteger();

    @QuerySqlField(index = true)
    private Integer empno;
    @QuerySqlField
    private String ename;
    @QueryTextField
    private String job;
    @QuerySqlField
    private Integer mgr;
    @QuerySqlField
    private LocalDate hiredate;
    @QuerySqlField
    private Integer sal;
    @QuerySqlField(index = true)
    private Integer deptno;

    private transient EmployeeKey key;

    public Employee(String ename, Department dept, String job, Integer mgr, LocalDate hiredate, Integer sal) {
        this.empno = GENERATED_ID.incrementAndGet();
        this.ename = ename;
        this.job = job;
        this.mgr = mgr;
        this.hiredate = hiredate;
        this.sal = sal;
        this.deptno = dept.getDeptno();
    }

    public Integer getEmpno() {
        return empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getMgr() {
        return mgr;
    }

    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    public LocalDate getHiredate() {
        return hiredate;
    }

    public void setHiredate(LocalDate hiredate) {
        this.hiredate = hiredate;
    }

    public Integer getSal() {
        return sal;
    }

    public void setSal(Integer sal) {
        this.sal = sal;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    //Affinity employee key
    public EmployeeKey getKey() {
        if (key == null) {
            key = new EmployeeKey(empno, deptno);
        }
        return key;
    }

    @Override public String toString() {
        return "Employee[" +
            "    ename='" + ename + '\'' +
            ", job='" + job + '\'' +
            ", mgr=" + mgr +
            ", hiredate=" + hiredate +
            ", sal=" + sal +
            ']';
    }
}
