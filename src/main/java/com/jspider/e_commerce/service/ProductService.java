package com.jspider.e_commerce.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.jspider.e_commerce.exception.ProductException;
import com.jspider.e_commerce.model.Product;
import com.jspider.e_commerce.request.CreateProductRequest;

public interface ProductService {
	
	public Product createProduct(CreateProductRequest req);
	
	public String deleteProduct(Long productId) throws ProductException;
	
	public Product UpdateProduct(Long productId,Product req) throws ProductException;
	
	public List<Product> getAllProducts();
	
	public Product getAllProductById(Long id) throws ProductException;
	
	public List<Product> findProductByCategory(String category);
	
	public Product findProductById(Long productId) throws ProductException;
	
	public Page<Product> getAllProduct(String category, List<String> colors, List<String>sizes,
			Integer minPrice,Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber,Integer pageSize);
		
	public List<Product> recentlyAddedProduct();	
	
	
	

}
