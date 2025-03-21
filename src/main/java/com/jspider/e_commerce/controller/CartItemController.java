package com.jspider.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jspider.e_commerce.exception.CartItemException;
import com.jspider.e_commerce.exception.UserException;
import com.jspider.e_commerce.model.CartItem;
import com.jspider.e_commerce.model.User;
import com.jspider.e_commerce.response.ApiResponse;
import com.jspider.e_commerce.service.CartItemService;
import com.jspider.e_commerce.service.UserService;


@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {
	@Autowired
	private CartItemService cartItemService;
	@Autowired
	private UserService userService;
	
//	public CartItemController(CartItemService cartItemService,UserService userService) {
//		this.cartItemService=cartItemService;
//		this.userService=userService;
//	}
	
	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<ApiResponse>deleteCartItemHandler(@PathVariable Long cartItemId, @RequestHeader("Authorization")String jwt) throws CartItemException, UserException{
		
		User user=userService.findUserProfileByJwt(jwt);
//		System.out.println("userid - - " + user.getFirstName());
		cartItemService.removeCartItem(user.getUserId(), cartItemId);
		
		ApiResponse res=new ApiResponse("Item Removed From Cart",true);
		
		return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{cartItemId}")
	public ResponseEntity<CartItem>updateCartItemHandler(@PathVariable Long cartItemId, @RequestBody CartItem cartItem, @RequestHeader("Authorization")String jwt) throws CartItemException, UserException{
		
		User user=userService.findUserProfileByJwt(jwt);
		
		CartItem updatedCartItem =cartItemService.updateCartItem(user.getUserId(), cartItemId, cartItem);
		
		//ApiResponse res=new ApiResponse("Item Updated",true);
		
		return new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
	}
	
	
}
