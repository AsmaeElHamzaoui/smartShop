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

    // CREATE COMMANDE
    @Transactional
    public CommandeDto create(CommandeDto dto) {

        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        // Mapper DTO vers entity (sans orderItems encore)
        Commande entity = mapper.toEntity(dto);
        entity.setClient(client);
        entity.setDate(LocalDate.now());
        entity.setStatut(OrderStatus.PENDING);

        // --- Créer et lier les OrderItems ---
        List<OrderItem> items = dto.getOrderItems().stream().map(itemDto -> {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable"));

            // Vérifier stock
            if (product.getStockDisponible() < itemDto.getQuantite()) {
                entity.setStatut(OrderStatus.REJECTED);
                throw new RuntimeException("Stock insuffisant pour le produit " + product.getNom());
            }

            // Déduire du stock
            product.setStockDisponible(product.getStockDisponible() - itemDto.getQuantite());

            // Créer l'OrderItem
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantite(itemDto.getQuantite());
            item.setPrixUnitaire(product.getPrixUnitaire());
            item.setTotalLigne(product.getPrixUnitaire().multiply(BigDecimal.valueOf(itemDto.getQuantite())));
            item.setCommande(entity); // Lien automatique vers la commande

            return item;
        }).toList();

        entity.setOrderItems(items);

        // --- Calcul sous-total ---
        BigDecimal sousTotal = items.stream()
                .map(OrderItem::getTotalLigne)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        entity.setSousTotal(sousTotal);

        // --- Remise fidélité ---
        int remise = calculerRemise(client, sousTotal);
        entity.setRemise(remise);

        BigDecimal montantRemise = sousTotal.multiply(BigDecimal.valueOf(remise).divide(BigDecimal.valueOf(100)));

        // --- Montant HT après remise ---
        BigDecimal montantHT = sousTotal.subtract(montantRemise);

        // TVA configurable (default 20)
        BigDecimal tva = montantHT.multiply(TVA_RATE);
        entity.setTva(tva);

        // Total TTC
        BigDecimal total = montantHT.add(tva);
        entity.setTotal(total);

        // Restant à payer
        entity.setMontantRestant(total);

        // --- Sauvegarde en base ---
        Commande saved = commandeRepository.save(entity);

        // --- Mapper vers DTO et retourner ---
        List<OrderItemDto> itemDtos = saved.getOrderItems().stream()
                .map(orderItemMapper::toDTO)
                .toList();

        CommandeDto resultDto = mapper.toDTO(saved);
        resultDto.setOrderItems(itemDtos);

        return resultDto;
    }



    // CONFIRMER COMMANDE
    @Transactional
    public CommandeDto confirmer(Integer id) {

        Commande cmd = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        if (cmd.getStatut() == OrderStatus.REJECTED)
            throw new RuntimeException("Commande rejetée, impossible de confirmer");

        cmd.setStatut(OrderStatus.CONFIRMED);
        cmd.setMontantRestant(BigDecimal.ZERO);

        // Mise à jour statistiques client
        Client client = cmd.getClient();

        long totalOrders = commandeRepository.count(); // simple

        BigDecimal totalSpent = clientTotalSpent(client.getId()).add(cmd.getTotal());

        // Mise à jour niveau fidélité
        client.setNiveauFidelite(
                calculerNiveauFidelite((int) totalOrders, totalSpent)
        );

        commandeRepository.save(cmd);
        clientRepository.save(client);

        return mapper.toDTO(cmd);
    }

    private BigDecimal clientTotalSpent(Integer clientId) {
        return commandeRepository.findAll().stream()
                .filter(c -> c.getClient().getId().equals(clientId) && c.getStatut() == OrderStatus.CONFIRMED)
                .map(Commande::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    // ANNULER COMMANDE
    public CommandeDto annuler(Integer id) {
        Commande cmd = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        cmd.setStatut(OrderStatus.CANCELED);
        return mapper.toDTO(commandeRepository.save(cmd));
    }


    // GET ALL
    public java.util.List<CommandeDto> getAll() {
        return commandeRepository.findAll()
                .stream().map(mapper::toDTO).toList();
    }


    // GET ONE
    public CommandeDto getById(Integer id) {
        Commande c = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
        return mapper.toDTO(c);
    }


    // Supprime (soft)
    public void delete(Integer id) {
        if (!commandeRepository.existsById(id))
            throw new RuntimeException("Commande introuvable");
        commandeRepository.deleteById(id);
    }


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

    // calcule niveau de fidilité
    private NiveauFidelite calculerNiveauFidelite(int totalOrders, BigDecimal totalSpent) {

        if (totalOrders >= 20 || totalSpent.compareTo(new BigDecimal("15000")) >= 0)
            return NiveauFidelite.PLATINUM;

        if (totalOrders >= 10 || totalSpent.compareTo(new BigDecimal("5000")) >= 0)
            return NiveauFidelite.GOLD;

        if (totalOrders >= 3 || totalSpent.compareTo(new BigDecimal("1000")) >= 0)
            return NiveauFidelite.SILVER;

        return NiveauFidelite.BASIC;
    }

}
