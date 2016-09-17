package com.blu.imdg.dto;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

/**
 * Created by shamim on 04/08/16.
 */
public class ServiceStatus implements Serializable{
    @QuerySqlField(index = true)
    private String serviceName;
    @QuerySqlField(index = true)
    private int statusCode;
    @QuerySqlField(index = true)
    private long responseTime;
    @QuerySqlField(index = true)
    private long unavailableCnt;
    @QuerySqlField(index = true)
    private long healthCheckCnt;

    public ServiceStatus(String name, int statusCode){
        this.serviceName = name;
        this.statusCode =statusCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public long getUnavailableCnt() {
        return unavailableCnt;
    }

    public void setUnavailableCnt(long unavailableCnt) {
        this.unavailableCnt = unavailableCnt;
    }

    public long getHealthCheckCnt() {
        return healthCheckCnt;
    }

    public void setHealthCheckCnt(long healthCheckCnt) {
        this.healthCheckCnt = healthCheckCnt;
    }


    @Override
    public String toString() {
        return "ServiceStatus{" +
                "serviceName='" + serviceName + '\'' +
                ", statusCode=" + statusCode +
                ", responseTime=" + responseTime +
                ", unavailableCnt=" + unavailableCnt +
                ", healthCheckCnt=" + healthCheckCnt +
                '}';
    }
}
