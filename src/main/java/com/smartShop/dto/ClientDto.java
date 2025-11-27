package com.smartShop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smartShop.enums.NiveauFidelite;
import lombok.Data;

@Data
public class ClientDto {

    private Integer id;
    private Integer userId;
    private String nom;
    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private NiveauFidelite niveauFidelite;
}
