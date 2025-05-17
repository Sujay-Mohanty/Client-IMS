package com.client.model;

import java.util.ArrayList;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class User {
    private long id;

    private String name;
    private String email;
    private String contact;
    private String password;
    private String role;


    private Cart cart;
    private List<Invoice> invoices = new ArrayList<>();
}