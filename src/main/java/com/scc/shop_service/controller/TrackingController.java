package com.scc.shop_service.controller;

import com.scc.shop_service.entity.Order;
import com.scc.shop_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TrackingController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/track")
    public String trackingPage(){
        return "track";
    }

    @PostMapping("/track")
    public String trackOrder(
            @RequestParam Long orderId,
            @RequestParam String mobileNumber,
            Model model){
        try{
            Order order = orderService.getById(orderId);
            if(!order.getMobileNumber().equals(mobileNumber)) {
                model.addAttribute("error","Order ID and mobile number do not match");

            return "track";
            }
            model.addAttribute("order",order);

            return "tracking-result";
        }
        catch(Exception e){
            model.addAttribute("error","Order Not Found!");

            return "track";
        }
    }
}
