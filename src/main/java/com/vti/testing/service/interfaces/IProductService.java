package com.vti.testing.service.interfaces;

import java.util.List;

import com.vti.testing.dto.ProductDTO;
import com.vti.testing.formcreate.FormProductCreate;
import com.vti.testing.formupdate.FormProductUpdate;

import org.springframework.http.ResponseEntity;

public interface IProductService {

	public List<ProductDTO> getAllProducts();

	public ResponseEntity<?> getProductById(int productId);

	public ResponseEntity<?> updateProduct(int productId, FormProductUpdate form );

	public ResponseEntity<?> createProduct(FormProductCreate form);

	public ResponseEntity<?> deleteProduct(int productId);

}
