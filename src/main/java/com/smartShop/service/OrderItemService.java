package com.smartShop.service;

import com.smartShop.dto.OrderItemDto;
import com.smartShop.entity.Commande;
import com.smartShop.entity.OrderItem;
import com.smartShop.entity.Product;
import com.smartShop.mapper.OrderItemMapper;
import com.smartShop.repository.CommandeRepository;
import com.smartShop.repository.OrderItemRepository;
import com.smartShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CommandeRepository commandeRepository;
    private final OrderItemMapper mapper;

    // CREATE
    public OrderItemDto create(OrderItemDto dto) {

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable"));

        Commande commande = commandeRepository.findById(dto.getCommandeId())
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        OrderItem entity = mapper.toEntity(dto);
        entity.setProduct(product);
        entity.setCommande(commande);

        // Prix unitaire automatiquement du produit
        entity.setPrixUnitaire(product.getPrixUnitaire());

        // total = prixUnitaire × quantite
        entity.setTotalLigne(product.getPrixUnitaire()
                .multiply(BigDecimal.valueOf(dto.getQuantite())));

        OrderItem saved = orderItemRepository.save(entity);
        return mapper.toDTO(saved);
    }

    // READ ALL
    public List<OrderItemDto> getAll() {
        return orderItemRepository.findAll()
                .stream().map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // READ ONE
    public OrderItemDto getById(Integer id) {
        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem introuvable"));
        return mapper.toDTO(item);
    }


}
