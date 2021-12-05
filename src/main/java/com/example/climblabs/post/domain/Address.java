package com.example.climblabs.post.domain;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Embeddable
public class Address {

    private String city;

    private String zipCode;

    private String street;

    private String detailStreet;
}
