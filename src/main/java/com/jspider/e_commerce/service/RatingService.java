package com.jspider.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jspider.e_commerce.exception.ProductException;
import com.jspider.e_commerce.model.Rating;
import com.jspider.e_commerce.model.User;
import com.jspider.e_commerce.request.RatingRequest;



@Service
public interface RatingService {
public Rating createRating(RatingRequest req,User user) throws ProductException;
	
	public List<Rating> getProductsRating(Long productId);
}
