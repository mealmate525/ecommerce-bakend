package com.jspider.e_commerce.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jspider.e_commerce.exception.ProductException;
import com.jspider.e_commerce.model.Category;
import com.jspider.e_commerce.model.Product;
import com.jspider.e_commerce.repository.CategoryRepository;
import com.jspider.e_commerce.repository.ProductRepository;
import com.jspider.e_commerce.request.CreateProductRequest;

@Service 
public class ProductServiceImplementation implements ProductService {
	@Autowired
	private ProductRepository productRepository;
//	@Autowired
	private UserService userService;
	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Product createProduct(CreateProductRequest req) {
	System.out.println( "req "  + req);
	Category topLevel = categoryRepository.findByName(req.getTopLevelCategory());
	System.out.println("top " + topLevel);
	
	if(topLevel == null) {
		Category topLevelCategory = new Category();
		System.out.println("req.getTopLevelCategory() "+ req.getTopLevelCategory());
		topLevelCategory.setName(req.getTopLevelCategory());
		topLevelCategory.setLevel(1);
		System.out.println("top level "+topLevelCategory);
		topLevel = categoryRepository.save(topLevelCategory);
		
		
		
	}
	
	Category secondLevel=categoryRepository.
			findByNameAndParent(req.getSecondLevelCategory(),topLevel.getName());
	if(secondLevel==null) {
		
		Category secondLavelCategory=new Category();
		secondLavelCategory.setName(req.getSecondLevelCategory());
		secondLavelCategory.setParentCategory(topLevel);
		secondLavelCategory.setLevel(2);
		
		secondLevel= categoryRepository.save(secondLavelCategory);
	}

	Category thirdLevel=categoryRepository.findByNameAndParent(req.getThirdLevelCategory(),secondLevel.getName());
	if(thirdLevel==null) {
		
		Category thirdLevelCategory=new Category();
		thirdLevelCategory.setName(req.getThirdLevelCategory());
		thirdLevelCategory.setParentCategory(secondLevel);
		thirdLevelCategory.setLevel(3);
		
		thirdLevel=categoryRepository.save(thirdLevelCategory);
	}
	
	System.out.println("first " + topLevel );
	System.out.println("second " + secondLevel );
	System.out.println("third " + thirdLevel );

	Product product=new Product();
	product.setTitle(req.getTitle());
	product.setColor(req.getColor());
	product.setDescription(req.getDescription());
	product.setDiscountedPrice(req.getDiscountedPrice());
	product.setDiscountPercent(req.getDiscountPercent());
	product.setImageUrl(req.getImageUrl());
	product.setBrand(req.getBrand());
	product.setPrice(req.getPrice());
	product.setSizes(req.getSizes());
	product.setQuantity(req.getQuantity());
	product.setCategory(thirdLevel);
	product.setCreatedAt(LocalDateTime.now());
	
	Product savedProduct= productRepository.save(product);
	
	System.out.println("products - "+product);
	
	return savedProduct;
	}

	@Override
	public String deleteProduct(Long productId) throws ProductException {
Product product=  findProductById(productId);
		
//		System.out.println("delete product "+product.getId()+" - "+productId);
		product.getSizes().clear();
		productRepository.delete(product);
		
		return "Product deleted Successfully";
	}

	@Override
	public Product UpdateProduct(Long productId, Product req) throws ProductException {
		
		Product product=  findProductById(productId);
		
		if(req.getQuantity()!=0 ) {
			product.setQuantity(req.getQuantity());
		}
		
		

		
		return productRepository.save(product);
	}

	@Override
	public Product getAllProductById(Long id) throws ProductException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public List<Product> findProductByCategory(String category) {
		
		return null;
	}
	
	@Override
	public Product findProductById(Long productId) throws ProductException {
//		System.out.println("******************************");
		Optional<Product> opt = productRepository.findById(productId);
//		System.out.println("++++++++++++++++++++++++++++++++++++");
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new ProductException("Product not found with id " + productId);
	}

	@Override
	public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		
		
		List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
		
		
		if (!colors.isEmpty()) {
			products = products.stream()
			        .filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
			        .collect(Collectors.toList());
		
		
		} 

		if(stock!=null) {

			if(stock.equals("in_stock")) {
				products=products.stream().filter(p->p.getQuantity()>0).collect(Collectors.toList());
			}
			else if (stock.equals("out_of_stock")) {
				products=products.stream().filter(p->p.getQuantity()<1).collect(Collectors.toList());				
			}
				
					
		}
		int startIndex = (int) pageable.getOffset();
		int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

		List<Product> pageContent = products.subList(startIndex, endIndex);
		Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());
	    return filteredProducts; // If color list is empty, do nothing and return all products
		
		
		
	}

	@Override
	public List<Product> recentlyAddedProduct() {
		
		return productRepository.findTop10ByOrderByCreatedAtDesc();
	}
	

}
