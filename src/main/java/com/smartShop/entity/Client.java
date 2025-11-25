package com.smartShop.entity;
import com.smartShop.enums.NiveauFidelite;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private String nom;

    private String email;

    @Enumerated(EnumType.STRING)
    private NiveauFidelite niveauFidelite = NiveauFidelite.BASIC; // valeur par défaut
}
