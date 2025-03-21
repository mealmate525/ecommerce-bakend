package com.jspider.e_commerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jspider.e_commerce.exception.OrderException;
import com.jspider.e_commerce.model.Address;
import com.jspider.e_commerce.model.Cart;
import com.jspider.e_commerce.model.CartItem;
import com.jspider.e_commerce.model.Order;
import com.jspider.e_commerce.model.OrderItem;
import com.jspider.e_commerce.model.User;
import com.jspider.e_commerce.repository.AddressRepository;
import com.jspider.e_commerce.repository.CartRepository;
import com.jspider.e_commerce.repository.OrderItemRepository;
import com.jspider.e_commerce.repository.OrderRepository;
import com.jspider.e_commerce.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
public class OrderServiceImplementation implements OrderService{
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CartService cartService;
	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Override
	public Order createOrder(User user, Address shippingAddress) {
		shippingAddress.setUser(user);
		Address address= addressRepository.save(shippingAddress);
		user.getAddresses().add(address);
		userRepository.save(user);
		
		Cart cart=cartService.findUserCart(user.getUserId());
		List<OrderItem> orderItems=new ArrayList<>();
		
		for(CartItem item: cart.getCartItems()) {
			OrderItem orderItem=new OrderItem();
			
			orderItem.setPrice(item.getPrice());
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setSize(item.getSize());
			orderItem.setUserId(item.getUserId());
			orderItem.setDiscountedPrice(item.getDiscountedPrice());
			
			
			OrderItem createdOrderItem=orderItemRepository.save(orderItem);
			
			orderItems.add(createdOrderItem);
		}
		
		
		Order createdOrder=new Order();
		createdOrder.setUser(user);
		createdOrder.setOrderItems(orderItems);
		createdOrder.setTotalPrice(cart.getTotalPrice());
		createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
		createdOrder.setDiscount(cart.getDiscount());
		createdOrder.setTotalItem(cart.getTotalItem());
		
		createdOrder.setShippingAddress(address);
		createdOrder.setOrderDate(LocalDateTime.now());
//		createdOrder.setOrderStatus(OrderStatus.PENDING);
		createdOrder.setOrderStatus("PENDING");
//		createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
		createdOrder.getPaymentDetails().setStatus("PENDING");

		createdOrder.setCreatedAt(LocalDateTime.now());
		
		Order savedOrder=orderRepository.save(createdOrder);
		
		for(OrderItem item:orderItems) {
			item.setOrder(savedOrder);
			orderItemRepository.save(item);
		}
		
		return savedOrder;
	}

	@Override
	public Order placedOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
//		order.setOrderStatus(OrderStatus.PLACED);
		order.setOrderStatus("PLACED");
//		order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
		order.getPaymentDetails().setStatus("COMPLETED");

		return order;
	}

	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
//		order.setOrderStatus(OrderStatus.CONFIRMED);
		order.setOrderStatus("CONFIRMED");

		
		return orderRepository.save(order);
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
//		order.setOrderStatus(OrderStatus.SHIPPED);
		order.setOrderStatus("SHIPPED");

		return orderRepository.save(order);
	}

	@Override
	public Order deliveredOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
//		order.setOrderStatus(OrderStatus.DELIVERED);
		order.setOrderStatus("DELIVERED");

		return orderRepository.save(order);
	}

	@Override
	public Order cancledOrder(Long orderId) throws OrderException {
		Order order=findOrderById(orderId);
//		order.setOrderStatus(OrderStatus.CANCELLED);
		order.setOrderStatus("CANCELLED");
		return orderRepository.save(order);
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException {
		Optional<Order> opt=orderRepository.findById(orderId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new OrderException("order not exist with id "+orderId);
	}

	@Override
	public List<Order> usersOrderHistory(Long userId) {
		List<Order> orders=orderRepository.getUsersOrders(userId);
		return orders;
	}

	@Override
	public List<Order> getAllOrders() {
		
		return orderRepository.findAllByOrderByCreatedAtDesc();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		Order order =findOrderById(orderId);
		
		orderRepository.deleteById(orderId);
		
	}
}
