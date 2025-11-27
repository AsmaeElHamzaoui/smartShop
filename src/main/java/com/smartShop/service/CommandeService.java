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


    // READ ONE
    public CommandeDto getById(Integer id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée : " + id));
        return commandeMapper.toDTO(commande);
    }

    // READ ALL
    public List<CommandeDto> getAll() {
        return commandeRepository.findAll()
                .stream()
                .map(commandeMapper::toDTO)
                .collect(Collectors.toList());
    }

    // UPDATE
    public CommandeDto update(Integer id, CommandeDto dto) {

        Commande existing = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée : " + id));

        // On ne modifie pas le client via update (mais si tu veux le permettre je peux l'ajouter)
        existing.setDate(dto.getDate());
        existing.setSousTotal(dto.getSousTotal());
        existing.setRemise(dto.getRemise());
        existing.setTva(dto.getTva());
        existing.setTotal(dto.getTotal());
        existing.setCodePromo(dto.getCodePromo());
        existing.setStatut(dto.getStatut());
        existing.setMontantRestant(dto.getMontantRestant());

        Commande updated = commandeRepository.save(existing);
        return commandeMapper.toDTO(updated);
    }

    // DELETE
    public void delete(Integer id) {
        if (!commandeRepository.existsById(id)) {
            throw new RuntimeException("Commande introuvable : " + id);
        }
        commandeRepository.deleteById(id);
    }


}
