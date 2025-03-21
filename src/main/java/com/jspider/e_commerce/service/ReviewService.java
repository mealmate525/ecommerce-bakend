package com.jspider.e_commerce.service;

import java.util.List;

import com.jspider.e_commerce.exception.ProductException;
import com.jspider.e_commerce.model.Review;
import com.jspider.e_commerce.model.User;
import com.jspider.e_commerce.request.ReviewRequest;



public interface ReviewService {
	public Review createReview(ReviewRequest req,User user) throws ProductException;
	
	public List<Review> getAllReview(Long productId);
}
