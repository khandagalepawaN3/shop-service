package com.scc.shop_service.dto;

import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CartItem {
    private Long productId;

    private String productName;

    private Double price;

    private Integer quantity;
}
