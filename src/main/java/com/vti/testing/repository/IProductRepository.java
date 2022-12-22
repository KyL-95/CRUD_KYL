package com.vti.testing.repository;

import com.vti.testing.entity.IProductMaxPrice;
import com.vti.testing.entity.Product;

import com.vti.testing.entity.ProductMinPrice;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
	public boolean existsByProductName(String productName);
	<T> List<T> findBy(Class<T> classType);
	@Query("SELECT new com.vti.testing.entity.ProductMinPrice(p.productName, min(p.productPrice)) FROM Product p"
			)
	List<ProductMinPrice> getProductMinPrice();
	@Query(value = "SELECT p.productName AS productName, MAX(p.productPrice) AS productPrice FROM product AS p",
			nativeQuery = true)
	List<IProductMaxPrice> getProductMaxPrice();

}
