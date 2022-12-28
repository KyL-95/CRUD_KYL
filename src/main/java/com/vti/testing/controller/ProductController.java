package com.vti.testing.controller;

import com.vti.testing.dto.ProductDTO;
import com.vti.testing.entity.IProductMaxPrice;
import com.vti.testing.entity.IProductProjection;
import com.vti.testing.entity.ProductMinPrice;
import com.vti.testing.formcreate.FormProductCreate;
import com.vti.testing.formupdate.FormProductUpdate;
import com.vti.testing.service.interfaces.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
//@PreAuthorize("hasAnyRole('ADMIN','MANAGER')") // Admin/Manager mới truy cập đc API trong class này
public class ProductController {
	@Autowired
	private IProductService productService;
	List<ProductDTO> dtos = null;
	@GetMapping("/getAll")
	public List<ProductDTO> getAllProducts() {
		long startTime = System.currentTimeMillis();
		dtos = productService.getAllProducts();
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		System.out.println("Time to get all  " + duration);
		return dtos;

	}
	@GetMapping("/getById/{id}")
	public ResponseEntity<?> getProductById(@PathVariable(name = "id") int productId) {
		long startTime = System.currentTimeMillis();
		ResponseEntity<?> result = productService.getProductById(productId);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		System.out.println("Time to get by id  " + duration);
		return result;

	}
	@GetMapping("/findAll")
	public List<IProductProjection> findAll(){
		return productService.findAll();
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
	@GetMapping("/getProductMinPrice")
	public List<ProductMinPrice> getProductMinPrice(){
		return productService.getProductMinPrice();
	}
	@GetMapping("/getProductMaxPrice")
	public List<IProductMaxPrice> getProductMaxPrice() {
		return productService.getProductMaxPrice();
	}
}
