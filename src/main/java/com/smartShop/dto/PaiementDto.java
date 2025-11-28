package com.smartShop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartShop.enums.PaymentStatus;
import com.smartShop.enums.TypePaiement;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaiementDto {

    private Integer id;
    private Integer commandeId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer numeroPaiement;

    private BigDecimal montant;
    private PaymentStatus statusPaiement;
    private TypePaiement typePaiement;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime datePaiement;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dateEncaissement;
}
