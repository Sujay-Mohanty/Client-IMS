package com.client.model;

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

public class InvoiceItem {

    private Long id;


    @JsonIgnore
    private Invoice invoice;


    @JsonIgnore
    private Product product;

    private int quantity;
    private double unitPrice;
}
