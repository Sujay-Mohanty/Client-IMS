package com.client.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderRequestDTO {

    private Long vendorId;
    private int quantity;
    private double price;
}
