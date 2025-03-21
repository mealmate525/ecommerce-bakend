package com.jspider.e_commerce.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jspider.e_commerce.exception.OrderException;
import com.jspider.e_commerce.model.Order;
import com.jspider.e_commerce.repository.OrderRepository;
import com.jspider.e_commerce.response.ApiResponse;
import com.jspider.e_commerce.response.PaymentLinkResponse;
import com.jspider.e_commerce.service.OrderService;
import com.jspider.e_commerce.service.UserService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api")
public class PaymentController {

    // Fetch Razorpay API key and secret from application properties
    @Value("${razorpay.api.key}")
    private String apiKey;
    
    @Value("${razorpay.api.secret}")
    private String apiSecret;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderRepository orderRepository;
    
    // Endpoint to create a Razorpay payment link for an order
    @PostMapping("/payments/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException, RazorpayException {
        
        // Fetch order details using orderId
        Order order = orderService.findOrderById(orderId);
        
        try {
            // Initialize Razorpay client with API credentials
            RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
            
            // Create JSON object for payment link request
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", order.getTotalDiscountedPrice() * 100); // Convert amount to paise (smallest unit in INR)
            paymentLinkRequest.put("currency", "INR");
            
            // Customer details
            JSONObject customer = new JSONObject();
            customer.put("name", order.getUser().getFirstName());
            customer.put("email", order.getUser().getEmail());
            paymentLinkRequest.put("customer", customer);
            
            // Notification settings
            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);
            
            // Callback URL to redirect after payment completion
            paymentLinkRequest.put("callback_url", "http://localhost:3000/payments/" + orderId);
            paymentLinkRequest.put("callback_method", "get");
            
            // Create payment link using Razorpay API
            PaymentLink payment = razorpay.paymentLink.create(paymentLinkRequest);
            
            // Extract payment link details
            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");
            
            // Prepare response object
            PaymentLinkResponse res = new PaymentLinkResponse();
            res.setPayment_link_id(paymentLinkId);
            res.setPayment_link_url(paymentLinkUrl);
            
            return new ResponseEntity<>(res, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RazorpayException(e.getMessage());
        }    
    }
    
    // Endpoint to handle payment success redirection
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> redirect(@RequestParam(name="payment_id") String paymentId, @RequestParam(name="order_id") Long orderId) throws OrderException, RazorpayException {
        
        // Fetch order details using orderId
        Order order = orderService.findOrderById(orderId);
        
        // Initialize Razorpay client
        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
        
        try {
            // Fetch payment details using payment ID
            Payment payment = razorpay.payments.fetch(paymentId);
            
            // Check if payment is successful
            if (payment.get("status").equals("captured")) {
                // Update order payment details
                order.getPaymentDetails().setPaymentId(paymentId);
                order.getPaymentDetails().setStatus("COMPLETED");
                order.setOrderStatus("PLACED");
                
                // Save updated order in database
                orderRepository.save(order);
            }
            
            // Prepare API response
            ApiResponse res = new ApiResponse();
            res.setMessage("Your order has been placed successfully");
            res.setStatus(true);
            
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            throw new RazorpayException(e.getMessage());
        }
    }
}
