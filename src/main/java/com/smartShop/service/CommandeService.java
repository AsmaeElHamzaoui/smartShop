package com.smartShop.service;

import com.smartShop.dto.CommandeDto;
import com.smartShop.dto.OrderItemDto;
import com.smartShop.entity.*;
import com.smartShop.enums.NiveauFidelite;
import com.smartShop.enums.OrderStatus;
import com.smartShop.mapper.CommandeMapper;
import com.smartShop.mapper.OrderItemMapper;
import com.smartShop.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final CommandeMapper mapper;
    private final OrderItemMapper orderItemMapper;


    private static final BigDecimal TVA_RATE = new BigDecimal("0.20");




    // LOGIQUE FIDÉLITÉ

    // Calcule de remise
    private int calculerRemise(Client client, BigDecimal sousTotal) {

        return switch (client.getNiveauFidelite()) {
            case SILVER -> sousTotal.compareTo(new BigDecimal("500")) >= 0 ? 5 : 0;
            case GOLD -> sousTotal.compareTo(new BigDecimal("800")) >= 0 ? 10 : 0;
            case PLATINUM -> sousTotal.compareTo(new BigDecimal("1200")) >= 0 ? 15 : 0;
            default -> 0;
        };
    }


}
