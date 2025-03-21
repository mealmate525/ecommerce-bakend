package com.jspider.e_commerce.service;

import com.jspider.e_commerce.exception.ProductException;
import com.jspider.e_commerce.model.Cart;
import com.jspider.e_commerce.model.CartItem;
import com.jspider.e_commerce.model.User;
import com.jspider.e_commerce.request.AddItemRequest;

public interface CartService {
	public Cart createCart(User user);
	
	public CartItem addCartItem(Long userId,AddItemRequest req) throws ProductException;
	
	public Cart findUserCart(Long userId);

}
