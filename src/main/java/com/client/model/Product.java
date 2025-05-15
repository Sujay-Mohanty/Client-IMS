package com.client.model;


import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@Builder

public class Product {



    private long id;

    private String name;
    private String description;
    private int quantity;
    private double price;


    @JsonIgnore
    private Vendor vendor;


    @JsonIgnore
    private List<CartItem> cartItems = new ArrayList<>();


    @JsonIgnore
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
}

	

	
	
	
	