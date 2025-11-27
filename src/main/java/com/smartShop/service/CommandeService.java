package com.smartShop.service;


import com.smartShop.dto.CommandeDto;
import com.smartShop.entity.Client;
import com.smartShop.entity.Commande;
import com.smartShop.mapper.CommandeMapper;
import com.smartShop.repository.ClientRepository;
import com.smartShop.repository.CommandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final CommandeMapper commandeMapper;


    // CREATE
    public CommandeDto create(CommandeDto dto) {

        // Récupération du client depuis la base
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new RuntimeException("Client non trouvé : " + dto.getClientId()));

        // Mapping DTO → Entity
        Commande commande = commandeMapper.toEntity(dto);
        commande.setClient(client);

        Commande saved = commandeRepository.save(commande);
        return commandeMapper.toDTO(saved);
    }



}
