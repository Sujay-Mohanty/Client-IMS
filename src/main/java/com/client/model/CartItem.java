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
public class CartItem {

    private Long id;


    @JsonIgnore
    private Cart cart;

    private Product product;

    private int quantity;
}
