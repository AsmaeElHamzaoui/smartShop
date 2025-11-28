package com.smartShop.entity;

import com.smartShop.enums.PaymentStatus;
import com.smartShop.enums.TypePaiement;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "paiements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relation ManyToOne vers Commande
    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @Column(nullable = false)
    private Integer numeroPaiement; // numéro séquentiel pour la commande

    @Column(nullable = false)
    private BigDecimal montant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus statusPaiement = PaymentStatus.EN_ATTENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePaiement typePaiement;

    @Column(nullable = false, updatable = false)
    private LocalDateTime datePaiement; // READ_ONLY

    @Column(updatable = false)
    private LocalDateTime dateEncaissement; // READ_ONLY
}
