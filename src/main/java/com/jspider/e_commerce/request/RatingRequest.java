package com.jspider.e_commerce.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter


public class RatingRequest {
	
	private Long productId;
	private double rating;

}
