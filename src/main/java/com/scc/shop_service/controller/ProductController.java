package com.scc.shop_service.controller;

import com.scc.shop_service.entity.Product;
import com.scc.shop_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("products",
                service.getAllProducts());

        return "index";
    }
}
