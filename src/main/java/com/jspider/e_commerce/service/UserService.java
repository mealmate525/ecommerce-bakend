package com.jspider.e_commerce.service;

import com.jspider.e_commerce.exception.UserException;
import com.jspider.e_commerce.model.User;

public interface UserService {

	
	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
	
	
}
