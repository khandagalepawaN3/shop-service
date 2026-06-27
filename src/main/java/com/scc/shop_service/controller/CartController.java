package com.scc.shop_service.controller;

import com.scc.shop_service.dto.CartItem;
import com.scc.shop_service.entity.Product;
import com.scc.shop_service.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductRepository repository;

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session){

        Product product = repository.findById(id).orElse(null);

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if(cart==null){
            cart = new ArrayList<>();
        }

        boolean found = false;

        for(CartItem item: cart){
            if(item.getProductId().equals(product.getId())){
                item.setQuantity(item.getQuantity()+1);
                found = true;
                break;
            }
        }

        if(!found) {
            CartItem item = new CartItem();

            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setPrice(product.getPrice());
            item.setQuantity(1);

            cart.add(item);

            session.setAttribute("cart", cart);
        }
        return "redirect:/";
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model){
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        double total = 0;
        if(cart!=null){
            total = cart.stream()
                    .mapToDouble(item ->
                            item.getPrice() *
                            item.getQuantity())
                    .sum();
        }

        model.addAttribute("cart",cart);
        model.addAttribute("total",total);

        return "cart";
    }

    @GetMapping("/remove/{id}")
    public String removeItem(@PathVariable Long id, HttpSession session){
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if(cart!=null) {
            cart.removeIf(item ->
                    item.getProductId().equals(id));

            session.setAttribute("cart", cart);
        }

        return "redirect:/cart";
    }

    @GetMapping("/increase/{id}")
    public String increaseQuantity(@PathVariable Long id, HttpSession session){
        List<CartItem> cart =(List<CartItem>) session.getAttribute("cart");

        if(cart!=null){
            for(CartItem item: cart) {
                if (item.getProductId().equals(id)) {
                    item.setQuantity(
                            item.getQuantity() + 1);
                    break;
                }
            }
        }
        return "redirect:/cart";
    }

    @GetMapping("/decrease/{id}")
    public String decreaseQuantity(@PathVariable Long id, HttpSession session){
        List<CartItem> cart =(List<CartItem>) session.getAttribute("cart");

        if(cart!=null){

            CartItem removeItem = null;

            for(CartItem item: cart) {
                if (item.getProductId().equals(id)) {
                    item.setQuantity(
                            item.getQuantity() - 1);

                    if (item.getQuantity() <= 0) {
                        removeItem = item;
                    }
                    break;
                }
            }
        }
        return "redirect:/cart";
    }


}
