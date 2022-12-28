package com.vti.testing.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ProductMinPrice {
    private String productName;
    private int minProductPrice;
}
