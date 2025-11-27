package com.smartShop.dto;

import com.smartShop.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CommandeDto {

    private Integer id;
    private Integer clientId;
    private LocalDate date;
    private BigDecimal sousTotal;
    private Integer remise;
    private BigDecimal tva;
    private BigDecimal total;
    private String codePromo;
    private OrderStatus statut;
    private BigDecimal montantRestant;
}
