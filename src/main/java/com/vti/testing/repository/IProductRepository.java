package com.vti.testing.repository;

import com.vti.testing.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {
	public boolean existsByProductName(String productName);
}
