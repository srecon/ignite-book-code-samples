package com.blu.imdg.model;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

/**
 * Created by shamim on 08/07/17.
 */
public class Breed implements Serializable {

    @QuerySqlField(index = true)
    private Long id;

    @QuerySqlField(index = true)
    private String name;

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Breed{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
