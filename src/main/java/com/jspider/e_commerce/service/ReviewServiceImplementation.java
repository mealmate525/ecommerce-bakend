package com.jspider.e_commerce.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jspider.e_commerce.exception.ProductException;
import com.jspider.e_commerce.model.Product;
import com.jspider.e_commerce.model.Review;
import com.jspider.e_commerce.model.User;
import com.jspider.e_commerce.repository.ProductRepository;
import com.jspider.e_commerce.repository.ReviewRepository;
import com.jspider.e_commerce.request.ReviewRequest;



@Service
public class ReviewServiceImplementation implements ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	private ProductService productService;
	@Autowired
	private ProductRepository productRepository;
	@Override
	
	
	
	public Review createReview(ReviewRequest req, User user) throws ProductException {
		Product product=productService.findProductById(req.getProductId());
		
		Review review=new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
		
//		product.getReviews().add(review);
		productRepository.save(product);
		return reviewRepository.save(review);
	}
	@Override
	public List<Review> getAllReview(Long productId) {
		return reviewRepository.getAllProductsReview(productId);
	}
}
