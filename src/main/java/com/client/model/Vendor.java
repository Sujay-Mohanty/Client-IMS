package com.client.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class Vendor {

    private long id;

    private String name;
    private String location;
    private String contact;


    private Product product;


    @JsonIgnore
    private List<Invoice> invoices = new ArrayList<>();


}