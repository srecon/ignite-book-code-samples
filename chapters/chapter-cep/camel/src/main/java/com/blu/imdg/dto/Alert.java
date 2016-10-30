package com.blu.imdg.dto;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * Created by shamim on 05/08/16.
 */
public class Alert {
    @QuerySqlField(index = true)
    private String serviceName;
    @QuerySqlField(index = true)
    private Level alertLevel;

    public Alert(String serviceName, Level alertLevel) {
        this.serviceName = serviceName;
        this.alertLevel = alertLevel;
    }

    public Level getAlertLevel() {
        return alertLevel;
    }

    public void setAlertLevel(Level alertLevel) {
        this.alertLevel = alertLevel;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "serviceName='" + serviceName + '\'' +
                ", alertLevel='" + alertLevel + '\'' +
                '}';
    }
}
