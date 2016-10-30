package com.blu.imdg.model;

import java.io.Serializable;
import java.util.Objects;
import org.apache.ignite.cache.affinity.AffinityKeyMapped;

/**
 * Created by isatimur on 8/10/16.
 */
public class EmployeeKey implements Serializable {

    private final int empNo;

    @AffinityKeyMapped
    private final int deptNo;

    public EmployeeKey(int empNo, int deptNo) {
        this.empNo = empNo;
        this.deptNo = deptNo;
    }

    public int getEmpNo() {
        return empNo;
    }

    public int getDeptNo() {
        return deptNo;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        EmployeeKey key = (EmployeeKey)o;
        return empNo == key.empNo &&
            deptNo == key.deptNo;
    }

    @Override public int hashCode() {
        return Objects.hash(empNo, deptNo);
    }

    @Override public String toString() {
        return "EmployeeKey[" +
            "empNo=" + empNo +
            ", deptNo=" + deptNo +
            ']';
    }
}
