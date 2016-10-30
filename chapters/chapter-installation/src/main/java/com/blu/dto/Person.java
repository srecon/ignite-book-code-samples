package com.blu.dto;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;

/**
 * Created by shamim on 19/10/16.
 */
public class Person implements Serializable
{

    @QuerySqlField(index = true)
    private String name;

    @QuerySqlField(index = true)
    private int age;

    public Person(int age, String name)
    {
        this.age = age;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
