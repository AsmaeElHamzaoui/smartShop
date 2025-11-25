package com.smartShop.service;

import com.smartShop.dto.ClientDto;
import com.smartShop.entity.Client;
import com.smartShop.enums.NiveauFidelite;
import com.smartShop.mapper.ClientMapper;
import com.smartShop.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    // CREATE
    public ClientDto create(ClientDto dto) {

        Client client = clientMapper.toEntity(dto);

        // valeur par défaut si nouveau client
        if (client.getNiveauFidelite() == null) {
            client.setNiveauFidelite(NiveauFidelite.BASIC);
        }

        return clientMapper.toDTO(clientRepository.save(client));
    }

    // READ all
    public List<ClientDto> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDTO)
                .toList();
    }

    // READ by ID
    public ClientDto getById(Integer id) {
        return clientRepository.findById(id)
                .map(clientMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
    }

    // UPDATE
    public ClientDto update(Integer id, ClientDto dto) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        existing.setNom(dto.getNom());
        existing.setEmail(dto.getEmail());
        existing.setUserId(dto.getUserId());
        existing.setNiveauFidelite(dto.getNiveauFidelite());

        return clientMapper.toDTO(clientRepository.save(existing));
    }

    // DELETE
    public void delete(Integer id) {
        clientRepository.deleteById(id);
    }
}
