package com.jspider.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspider.e_commerce.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
