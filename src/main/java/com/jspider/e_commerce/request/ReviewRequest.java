package com.jspider.e_commerce.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewRequest {
	private Long productId;
	private String review;
}
