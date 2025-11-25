package com.smartShop.dto;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Integer id;
    private String nom;
    private BigDecimal prixUnitaire;
    private Integer stockDisponible;
}

