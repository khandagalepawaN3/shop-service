package com.scc.shop_service.controller;

import com.scc.shop_service.dto.CartItem;
import com.scc.shop_service.entity.OrderItem;
import com.scc.shop_service.service.OrderService;
import com.scc.shop_service.entity.Order;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public String placeOrder(
            @RequestParam String customerName,
            @RequestParam String mobileNumber, HttpSession session){

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if(cart==null || cart.isEmpty()){
            return "redirect:/cart";
        }

        List<OrderItem> orderItems = new ArrayList<>();

        double total = 0;


        for(CartItem cartItem: cart){
            OrderItem item = new OrderItem();

            item.setProductName(cartItem.getProductName());
            item.setPrice(cartItem.getPrice());
            item.setQuantity(cartItem.getQuantity());
            orderItems.add(item);
        }

        if(cart != null){
            total = cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        }

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setMobileNumber(mobileNumber);
        order.setTotalAmount(total);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());
        order.setItems(orderItems);
        Order savedOrder = orderService.save(order);

        session.removeAttribute("cart");
        return "redirect:/order/success/" + savedOrder.getId();
        }

        @GetMapping("/success/{id}")
        public String orderSuccess(@PathVariable Long id, Model model){
            Order order = orderService.getById(id);
            model.addAttribute("order",order);

            return "order-success";
        }
    }
