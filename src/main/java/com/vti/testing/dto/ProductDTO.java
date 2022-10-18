package com.vti.testing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {

	@JsonProperty("ID")
	private int productId;
	@JsonProperty("Name")
	private String productName;
	@JsonProperty("Price")
	private int productPrice;
	private int categoryId;

	public ProductDTO(int productId, String productName, int productPrice, int categoryId) {
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
		this.categoryId = categoryId;
	}

}
