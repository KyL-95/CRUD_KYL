package com.vti.testing.service.interfaces;

import java.util.List;

import com.vti.testing.dto.ProductDTO;
import com.vti.testing.entity.IProductMaxPrice;
import com.vti.testing.entity.IProductProjection;
import com.vti.testing.entity.ProductMinPrice;
import com.vti.testing.formcreate.FormProductCreate;
import com.vti.testing.formupdate.FormProductUpdate;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;

public interface IProductService {

	 List<ProductDTO> getAllProducts();

	 ResponseEntity<?> getProductById(int productId);

	 ResponseEntity<?> updateProduct(int productId, FormProductUpdate form );

	 ResponseEntity<?> createProduct(FormProductCreate form);

	 ResponseEntity<?> deleteProduct(int productId);
	 List<IProductProjection> findAll();
	List<ProductMinPrice> getProductMinPrice();
	List<IProductMaxPrice> getProductMaxPrice() ;
}
