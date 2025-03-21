package com.jspider.e_commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jspider.e_commerce.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
