package com.client.dto;

import com.client.model.Product;
import com.client.model.Vendor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VendorProductDTO {
	private Vendor vendor;
	private Product product;
}
