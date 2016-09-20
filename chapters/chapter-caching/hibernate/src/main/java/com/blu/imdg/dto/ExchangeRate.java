package com.blu.imdg.dto;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * User: bsha
 * Date: 03.07.2016
 * Time: 10:29
 */
@Entity
@Table( name = "emp" )
public class ExchangeRate {
    @Id
    @Column(name = "region")
    private String region;
    @Column(name = "ratedate")
    private Date rateDate;
    @Column(name = "usdollar")
    private double usdollar;
    @Column(name = "euro")
    private double euro;
    @Column(name = "gbp")
    private double gbp;

    public ExchangeRate() {
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getRateDate() {
        return rateDate;
    }

    public void setRateDate(Date rateDate) {
        this.rateDate = rateDate;
    }

    public double getUsdollar() {
        return usdollar;
    }

    public void setUsdollar(double usdollar) {
        this.usdollar = usdollar;
    }

    public double getEuro() {
        return euro;
    }

    public void setEuro(double euro) {
        this.euro = euro;
    }

    public double getGbp() {
        return gbp;
    }

    public void setGbp(double gbp) {
        this.gbp = gbp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExchangeRate that = (ExchangeRate) o;

        if (!rateDate.equals(that.rateDate)) return false;
        if (!region.equals(that.region)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = region.hashCode();
        result = 31 * result + rateDate.hashCode();
        return result;
    }
}
