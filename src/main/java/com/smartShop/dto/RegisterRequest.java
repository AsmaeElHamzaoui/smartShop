package com.smartShop.dto;

import com.smartShop.enums.Role;
import lombok.Data;

@Data
public class RegisterRequest { // seulement pour l'entrée (pour saisir le mot de passe
    private String username;
    private String password;
    private Role role;
}
