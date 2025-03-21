package com.jspider.e_commerce.service;

import com.jspider.e_commerce.exception.CartItemException;
import com.jspider.e_commerce.exception.UserException;
import com.jspider.e_commerce.model.Cart;
import com.jspider.e_commerce.model.CartItem;
import com.jspider.e_commerce.model.Product;

public interface CartItemService {
	public CartItem createCartItem(CartItem cartItem);
	
	public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws CartItemException, UserException;
	
	public CartItem isCartItemExist(Cart cart,Product product,String size, Long userId); // if we have item in cart then we have simply
//	update the item number so we have made this method
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException; 
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;
	

}
