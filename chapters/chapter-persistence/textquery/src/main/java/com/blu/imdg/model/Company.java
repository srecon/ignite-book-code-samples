package com.blu.imdg.model;

import org.apache.ignite.cache.query.annotations.QueryTextField;

import java.io.Serializable;

/**
 * Created by isatimur on 8/9/16.
 */
public class Company implements Serializable {

    private Long id;
    private String cat;
    @QueryTextField
    private String companyName;
    @QueryTextField
    private String email;
    @QueryTextField
    private String address;
    @QueryTextField
    private String city;
    @QueryTextField
    private String state;
    @QueryTextField
    private String zipCode;
    private String phoneNumber;
    private String faxNumber;
    private String sicCode;
    private String sicDescription;
    @QueryTextField
    private String webAddress;


    public Company(Long id, String cat, String companyName, String email, String address, String city, String state, String zipCode, String phoneNumber, String faxNumber, String sicCode, String sicDescription, String webAddress) {
        this.id = id;
        this.cat = cat;
        this.companyName = companyName;
        this.email = email;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        this.faxNumber = faxNumber;
        this.sicCode = sicCode;
        this.sicDescription = sicDescription;
        this.webAddress = webAddress;
    }

    public Long getId() {
        return id;
    }

    public String getCat() {
        return cat;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public String getSicCode() {
        return sicCode;
    }

    public String getSicDescription() {
        return sicDescription;
    }

    public String getWebAddress() {
        return webAddress;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", cat='" + cat + '\'' +
                ", companyName='" + companyName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", faxNumber='" + faxNumber + '\'' +
                ", sicCode='" + sicCode + '\'' +
                ", sicDescription='" + sicDescription + '\'' +
                ", webAddress='" + webAddress + '\'' +
                '}';
    }
}
