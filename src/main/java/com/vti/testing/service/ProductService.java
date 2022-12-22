package com.vti.testing.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vti.testing.dto.ProductDTO;
import com.vti.testing.entity.*;
import com.vti.testing.exception.custom_exception.AlreadyExistEx;
import com.vti.testing.exception.custom_exception.CategoryIdInvalidEx;
import com.vti.testing.exception.custom_exception.CategoryNameInvalidEx;
import com.vti.testing.exception.custom_exception.NotFoundEx;
import com.vti.testing.formcreate.FormProductCreate;
import com.vti.testing.formupdate.FormProductUpdate;
import com.vti.testing.repository.IProductRepository;
import com.vti.testing.responseobj.ResponseObj;

import com.vti.testing.service.interfaces.IProductService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProductService implements IProductService {
	private final Logger log = LoggerFactory.getLogger(ProductService.class);
	@Autowired
	private IProductRepository productRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	@Cacheable("product")
	public List<ProductDTO> getAllProducts() {
		List<Product> products = productRepository.findAll();
		List<ProductDTO> dtos = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {
		}.getType());
		return dtos.isEmpty() ? new ArrayList<>() : dtos;
	}

	@Override
//	@Cacheable("product")
	public ResponseEntity<?> getProductById(int productId) {
		// Kiểm tra nếu entityCheck mà chứa giá trị
		Optional<Product> entityCheck = productRepository.findById(productId);
		if (entityCheck.isPresent()) {
			// Thì lấy entity đó ra
			Product entity = productRepository.findById(productId).get();
			// Map entity -> dto
			ProductDTO dto = modelMapper.map(entity, ProductDTO.class);
			return ResponseEntity.ok(new ResponseObj("Ok", "Get a product successfully", dto));

		}
		throw new NotFoundEx("Can't find a product with id = " + productId);

	}

	@Override
	@CachePut(value ="product")
	public ResponseEntity<?> updateProduct(int productId, FormProductUpdate form) {
		// Check exists by id
		if (productRepository.existsById(productId) == false) {
			throw new NotFoundEx("Can't find a product to update with id = " + productId);
		}
		Product proUpdate = productRepository.findById(productId).get();
		// Check product name is used???
		if (productRepository.existsByProductName(form.getProductName().trim())) {
			throw new AlreadyExistEx("This name : " + form.getProductName() + " is used");
		}
		// Validate category id
		if (form.getCategoryId() != 1 && form.getCategoryId() != 2) {
			throw new CategoryIdInvalidEx("Category Id value is only 1 or 2 ");
		}
		proUpdate.setProductName(form.getProductName());
		proUpdate.setProductPrice(form.getProductPrice());
		proUpdate.setCategory(new Category(form.getCategoryId(), form.getCategoryName()));
		productRepository.save(proUpdate);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj("", "Update is Successfully", ""));
	}

	@Override
	@CachePut(value ="product")
	public ResponseEntity<?> createProduct(FormProductCreate form) {
		// Check productName user nhập đã tồn tại chưa?
		if (productRepository.existsByProductName(form.getProductName().trim())) {
			throw new AlreadyExistEx("This name : " + form.getProductName() + " is used");

		}
		// Validate category id
		if (form.getCategoryId() != 1 && form.getCategoryId() != 2) {
			throw new CategoryIdInvalidEx("Category Id value is only 1 or 2 ");

		}
		// Validate category name
		if((form.getCategoryId() == 1 && !form.getCategoryName().equals("apple")) || 
			form.getCategoryId() == 2 && !form.getCategoryName().equals("samsung"))	 {
			 throw new CategoryNameInvalidEx("Can't map categoryId to categoryName");
		}
		// Convert createform -> entity
		Category newCategory = new Category(form.getCategoryId(), form.getCategoryName());
		Product newProduct = new Product(form.getProductName(), form.getProductPrice(), newCategory);
		productRepository.save(newProduct);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj("", "Create is Successfully", ""));
	}

	@Override
	public ResponseEntity<?> deleteProduct(int productId) {
		// Check exists by id
		if (productRepository.existsById(productId) == false) {
			throw new NotFoundEx("Can't find a product to update with id = " + productId);

		}
		productRepository.deleteById(productId);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseObj("", "Delete is Successfully", ""));
	}

	@Override
	public List<IProductProjection> findAll() {
		return productRepository.findBy(IProductProjection.class);
	}

	@Override
	public List<ProductMinPrice> getProductMinPrice() {
		return productRepository.getProductMinPrice();
	}

	@Override
	public List<IProductMaxPrice> getProductMaxPrice()  {
		List<IProductMaxPrice> result = productRepository.getProductMaxPrice();
		log.info("find max price product");
		return result;
	}

}
