package com.vti.testing.formcreate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormProductCreate {
	
	private String productName;
	private int productPrice;
	private int categoryId;
	private String categoryName;
}
