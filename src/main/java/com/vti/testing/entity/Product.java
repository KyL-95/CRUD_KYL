package com.vti.testing.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="product")
public class Product implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="productId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productId;
	
	@Column(name = "productName",nullable = false, unique = true )
	private String productName;
		
	@Column()
	private int productPrice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "categoryId")
	private Category category;

	public Product(String productName, int productPrice, Category category) {
		this.productName = productName;
		this.productPrice = productPrice;
		this.category = category;
	}
	


//	@Override
//	public String toString() {
//		return "Product [productId=" + productId + ", productName=" + productName + ", productPrice=" + productPrice
//				+ ", categoryId=" + category.getCategoryId() + "]";
//	}
	
	
}
