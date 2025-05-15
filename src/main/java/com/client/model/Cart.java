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

public class Cart {

    private long id;


    @JsonIgnore
    private User user;


    private List<CartItem> items = new ArrayList<>();
}