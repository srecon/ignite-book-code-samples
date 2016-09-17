package com.blu.imdg.dto;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * User: bsha
 * Date: 07.08.2016
 * Time: 15:29
 */
public class MnpRouting {
    @QuerySqlField(index = true)
    private String telephone;
    @QuerySqlField(index = true)
    private long    credit;
    @QuerySqlField(index = true)
    private String routeTo;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public long getCredit() {
        return credit;
    }

    public void setCredit(long credit) {
        this.credit = credit;
    }

    public String getRouteTo() {
        return routeTo;
    }

    public void setRouteTo(String routeTo) {
        this.routeTo = routeTo;
    }

    @Override
    public String toString() {
        return "Routing {" +
                "telephone='" + telephone + '\'' +
                ", credit=" + credit +
                ", routeTo='" + routeTo + '\'' +
                '}';
    }
}
