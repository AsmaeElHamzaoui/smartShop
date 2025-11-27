package com.smartShop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartShop.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CommandeDto {

    private Integer id;
    private Integer clientId;

    private LocalDate date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal sousTotal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer remise;

    //Tva : au départ 20 mais on peut la configurer
    private BigDecimal tva;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal total;

    private String codePromo;
    private OrderStatus statut;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal montantRestant;


    private List<OrderItemDto> orderItems;
}
