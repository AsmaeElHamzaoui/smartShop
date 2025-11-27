package com.smartShop.entity;

import com.smartShop.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relation ManyToOne vers Client
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private BigDecimal sousTotal;

    @Column
    private Integer remise; // % remise

    @Column(nullable = false)
    private BigDecimal tva;

    @Column(nullable = false)
    private BigDecimal total;

    @Column
    private String codePromo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus statut = OrderStatus.PENDING;

    @Column(nullable = false)
    private BigDecimal montantRestant;
}
