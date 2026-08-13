package com.scc.shop_service.controller;

import com.scc.shop_service.entity.Product;
import com.scc.shop_service.service.OrderService;
import com.scc.shop_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import java.util.Map;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProductService service;

    @Autowired
    private OrderService orderService;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping
    public String adminPage(Model model){

        model.addAttribute("product", new Product());

        model.addAttribute("products",service.getAllProducts());

        model.addAttribute("totalProducts",service.countProducts());

        model.addAttribute("totalOrders",orderService.countOrders());

        model.addAttribute("pendingOrders",orderService.countPendingOrders());

        model.addAttribute("completedOrders",orderService.countCompletedOrders());

        return "admin";
    }

    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute Product product,
            BindingResult result,
            Model model,
            @RequestParam("imageFile") MultipartFile imageFile
            ){

        if(result.hasErrors()){
            model.addAttribute(
                    "products",
                    service.getAllProducts());

            return "admin";
        }
        product.setAvailable(true);

        if (!imageFile.isEmpty()) {

            try {

                Map uploadResult = cloudinary.uploader().upload(
                        imageFile.getBytes(),
                        Map.of("folder", "scs-products")
                );

                String imageUrl =
                        uploadResult.get("secure_url").toString();

                product.setImageUrl(imageUrl);

            } catch (IOException e) {

                throw new RuntimeException(
                        "Could not upload image to Cloudinary", e);
            }
        }

        service.save(product);

        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id){

        service.delete(id);

        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id,Model model){
            Product product = service.getById(id);

            model.addAttribute("product",product);

            model.addAttribute("products",service.getAllProducts());

            return "admin";

    }

    @GetMapping("/orders")
    public String orders(Model model){
        model.addAttribute("orders",orderService.getAllOrders());

        return "orders";
    }

    @GetMapping("/orders/status/{id}/{status}")
    public String updateOrderStatus(
            @PathVariable Long id,
            @PathVariable String status){

        orderService.updateStatus(id,status);

        return "redirect:/admin/orders";
    }

}
