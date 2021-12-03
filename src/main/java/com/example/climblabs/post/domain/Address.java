package com.example.climblabs.post.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String city;

    private String zipCode;

    private String street;

    private String detailStreet;
}
