package com.smartShop.dto;

import com.smartShop.enums.NiveauFidelite;
import lombok.Data;

@Data
public class ClientDto {

    private Integer id;
    private Integer userId;
    private String nom;
    private String email;
    private NiveauFidelite niveauFidelite;
}
