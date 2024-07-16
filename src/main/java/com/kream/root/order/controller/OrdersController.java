package com.kream.root.order.controller;

import com.kream.root.Login.model.UserListDTO;
import com.kream.root.entity.AddressBook;
import com.kream.root.order.PaymentInfo;
import com.kream.root.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/create")
    public String createOrder(@RequestBody PaymentInfo paymentInfo) {
        try {
            orderService.handlePayment(paymentInfo);
            return "Order created successfully";
        } catch (Exception e) {
            return "Failed to create order: " + e.getMessage();
        }
    }

    @GetMapping("/user/{userId}")
    public UserListDTO getUserById(@PathVariable int userId) {
        return orderService.getUserById(userId);
    }

    @GetMapping("/address/{userId}")
    public List<AddressBook> getAddressBooksByUserId(@PathVariable int userId) {
        return orderService.getSortedAddressBooksByUserId(userId);
    }
    @PostMapping("/cancel")
    public String cancelPayment(@RequestParam String impUid, @RequestParam Long merchantUid, @RequestParam int userId) {
        try {
            orderService.cancelPayment(impUid, merchantUid, userId);
            return "Payment cancelled successfully";
        } catch (Exception e) {
            return "Failed to cancel payment: " + e.getMessage();
        }
    }

}
