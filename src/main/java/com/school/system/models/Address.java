package com.school.system.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipCode;
    private String country;


}
