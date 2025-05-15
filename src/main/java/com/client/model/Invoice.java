package com.client.model;

import java.time.LocalDateTime;
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
public class Invoice {

    private Long id;

    private double price;

    private String type; // "PURCHASE" or "SALES"

    private LocalDateTime dateTime;


    private User user; // only for SALES invoice


    private Vendor vendor; // only for PURCHASE invoice


    private List<InvoiceItem> items = new ArrayList<>();
}
