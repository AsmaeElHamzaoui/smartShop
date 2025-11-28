package com.smartShop.service;

import com.smartShop.dto.CommandeDto;
import com.smartShop.dto.OrderItemDto;
import com.smartShop.entity.*;
import com.smartShop.enums.NiveauFidelite;
import com.smartShop.enums.OrderStatus;
import com.smartShop.enums.PaymentStatus;
import com.smartShop.mapper.CommandeMapper;
import com.smartShop.mapper.OrderItemMapper;
import com.smartShop.repository.ClientRepository;
import com.smartShop.repository.CommandeRepository;
import com.smartShop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandeServiceTest {

    @Mock
    private CommandeRepository commandeRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CommandeMapper mapper;
    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private CommandeService service;

    private Client client;
    private Product product;
    private OrderItemDto itemDto;
    private CommandeDto commandeDto;
    private OrderItem orderItem;
    private Commande commande;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1);
        client.setNiveauFidelite(NiveauFidelite.BASIC);

        product = new Product();
        product.setId(1);
        product.setNom("Produit1");
        product.setPrixUnitaire(BigDecimal.valueOf(100));
        product.setStockDisponible(10);

        itemDto = new OrderItemDto();
        itemDto.setProductId(1);
        itemDto.setQuantite(2);

        commandeDto = new CommandeDto();
        commandeDto.setClientId(1);
        commandeDto.setOrderItems(List.of(itemDto));

        orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setQuantite(2);
        orderItem.setPrixUnitaire(product.getPrixUnitaire());
        orderItem.setTotalLigne(product.getPrixUnitaire().multiply(BigDecimal.valueOf(2)));

        commande = new Commande();
        commande.setId(1);
        commande.setClient(client);
        commande.setOrderItems(List.of(orderItem));
        commande.setSousTotal(orderItem.getTotalLigne());
        commande.setTotal(orderItem.getTotalLigne());
        commande.setMontantRestant(orderItem.getTotalLigne());
        commande.setStatut(OrderStatus.PENDING);
    }

    @Test
    void testCreateCommande_Success() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(mapper.toEntity(commandeDto)).thenReturn(new Commande());
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);
        when(mapper.toDTO(commande)).thenReturn(commandeDto);
        when(orderItemMapper.toDTO(orderItem)).thenReturn(itemDto);

        CommandeDto result = service.create(commandeDto);

        assertEquals(commandeDto.getClientId(), result.getClientId());
        verify(productRepository, times(1)).findById(1);
        verify(commandeRepository, times(1)).save(any());
    }

    @Test
    void testCreateCommande_StockInsuffisant() {
        product.setStockDisponible(1);
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(mapper.toEntity(commandeDto)).thenReturn(new Commande());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.create(commandeDto));
        assertTrue(ex.getMessage().contains("Stock insuffisant"));
    }

    @Test
    void testConfirmer_Success() {
        Paiement paiement = new Paiement();
        paiement.setMontant(commande.getTotal());
        paiement.setStatusPaiement(PaymentStatus.ENCAISSÉ);
        commande.setPaiements(List.of(paiement));
        when(commandeRepository.findById(1)).thenReturn(Optional.of(commande));
        when(commandeRepository.count()).thenReturn(1L);
        when(commandeRepository.findAll()).thenReturn(List.of(commande));
        when(mapper.toDTO(commande)).thenReturn(commandeDto);

        CommandeDto result = service.confirmer(1);

        assertEquals(OrderStatus.CONFIRMED, commande.getStatut());
        assertEquals(BigDecimal.ZERO, commande.getMontantRestant());
        assertEquals(commandeDto, result);
        verify(commandeRepository, times(1)).save(commande);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testConfirmer_CommandeRejected() {
        commande.setStatut(OrderStatus.REJECTED);
        when(commandeRepository.findById(1)).thenReturn(Optional.of(commande));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.confirmer(1));
        assertTrue(ex.getMessage().contains("Commande rejetée"));
    }


}
