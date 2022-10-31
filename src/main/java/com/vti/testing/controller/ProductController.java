package com.vti.testing.controller;

import java.util.List;

import com.vti.testing.author_anotations.IsUser;
import com.vti.testing.dto.ProductDTO;
import com.vti.testing.formcreate.FormProductCreate;
import com.vti.testing.formupdate.FormProductUpdate;
import com.vti.testing.service.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/product")
//@PreAuthorize("hasAnyRole('ADMIN')") // Admin mới truy cập đc API trong class này
public class ProductController {
	@Autowired
	private IProductService productService;

	List<ProductDTO> dtos = null;


	@GetMapping("/getAll")
	@IsUser
	public List<ProductDTO> getAllProducts() {
		dtos = productService.getAllProducts();
		return dtos;

	}

	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getProductById(@PathVariable(name = "id") int productId) {
		return productService.getProductById(productId);

	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateProductById(@PathVariable(name = "id") int productId,
			@RequestBody(required = true) FormProductUpdate form) {
		return productService.updateProduct(productId, form);

	}

	@PostMapping("/newProduct")
	public ResponseEntity<?> createProduct(@RequestBody(required = true) FormProductCreate form) {
		return productService.createProduct(form);

	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable(name = "id") int productId){
		return productService.deleteProduct(productId);
		
	}
}
