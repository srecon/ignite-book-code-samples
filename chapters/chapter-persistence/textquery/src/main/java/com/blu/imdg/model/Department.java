package com.blu.imdg.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * Created by isatimur on 8/9/16.
 */
public class Department implements Serializable {
    private static final AtomicInteger GENERATED_ID = new AtomicInteger();

    @QuerySqlField(index = true)
    private Integer deptno;

    @QuerySqlField
    private String dname;

    @QuerySqlField
    private String loc;

    public Department(String dname, String loc) {
        this.deptno = GENERATED_ID.incrementAndGet();
        this.dname = dname;
        this.loc = loc;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Department that = (Department)o;
        return Objects.equals(deptno, that.deptno) &&
            Objects.equals(dname, that.dname) &&
            Objects.equals(loc, that.loc);
    }

    @Override public int hashCode() {
        return Objects.hash(deptno, dname, loc);
    }

    @Override public String toString() {
        return "Department[" +
            "deptno=" + deptno +
            ", dname='" + dname + '\'' +
            ", loc='" + loc + '\'' +
            ']';
    }
}
