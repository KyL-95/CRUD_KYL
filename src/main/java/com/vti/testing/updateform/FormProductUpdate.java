package com.vti.testing.updateform;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormProductUpdate {
	
	private String productName;
	private int productPrice;
	private int categoryId;
	private String categoryName;
	

}
