package com.smartShop.service;

import com.smartShop.dto.ClientDto;
import com.smartShop.entity.Client;
import com.smartShop.entity.User;
import com.smartShop.enums.NiveauFidelite;
import com.smartShop.mapper.ClientMapper;
import com.smartShop.repository.ClientRepository;
import com.smartShop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final UserRepository userRepository;

    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClientDto getClientById(Integer id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return clientMapper.toDTO(client);
    }

    public ClientDto createClient(ClientDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Client client = clientMapper.toEntity(dto);
        client.setUser(user);

        // Niveau fidélité par défaut si null
        if (client.getNiveauFidelite() == null) {
            client.setNiveauFidelite(NiveauFidelite.BASIC);
        }

        Client saved = clientRepository.save(client);
        return clientMapper.toDTO(saved);
    }

    public ClientDto updateClient(Integer id, ClientDto dto) {
        Client existing = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        existing.setNom(dto.getNom());
        existing.setEmail(dto.getEmail());
        if (dto.getNiveauFidelite() != null) {
            existing.setNiveauFidelite(dto.getNiveauFidelite());
        }

        Client updated = clientRepository.save(existing);
        return clientMapper.toDTO(updated);
    }

    public void deleteClient(Integer id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client not found");
        }
        clientRepository.deleteById(id);
    }
}
