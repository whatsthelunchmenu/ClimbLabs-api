package com.example.climblabs.post.domain;

import javax.persistence.Embeddable;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Embeddable
public class Address {

    private String city;

    private String sido;

    private String zipCode;

    private String street;

    private String detailStreet;
}
