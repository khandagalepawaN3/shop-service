package com.scc.shop_service.service;

import com.scc.shop_service.entity.Order;
import com.scc.shop_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    public Order save(Order order){
        return repository.save(order);
    }

    public List<Order> getAllOrders(){
        return repository.findAll();
    }

    public Order getById(Long id){
        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order Not Found!"));
    }

    public void updateStatus(Long id,String status){
        Order order = getById(id);

        order.setStatus(status);

        repository.save(order);
    }

    public long countOrders(){
        return repository.count();
    }

    public long countPendingOrders(){
        return repository.countByStatus("PENDING");
    }

    public long countCompletedOrders(){
        return repository.countByStatus("COMPLETED");
    }

}
