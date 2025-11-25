package com.smartShop.controller;

import com.smartShop.dto.ClientDto;
import com.smartShop.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ClientDto create(@RequestBody ClientDto dto) {
        return clientService.create(dto);
    }

    @GetMapping
    public List<ClientDto> getAll() {
        return clientService.getAll();
    }

    @GetMapping("/{id}")
    public ClientDto getById(@PathVariable Integer id) {
        return clientService.getById(id);
    }

    @PutMapping("/{id}")
    public ClientDto update(@PathVariable Integer id, @RequestBody ClientDto dto) {
        return clientService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        clientService.delete(id);
    }
}
