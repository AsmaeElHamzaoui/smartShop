package com.smartShop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {

    private Integer id;

    private Integer produitId;
    private Integer commandeId;

    private Integer quantite;

    //Automatiquement va prendre le prix du produit commandé
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal prixUnitaire;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalLigne;
}
