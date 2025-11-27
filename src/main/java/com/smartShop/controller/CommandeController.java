package com.smartShop.controller;

import com.smartShop.dto.CommandeDto;
import com.smartShop.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService service;

    // CREATE
    @PostMapping
    public ResponseEntity<CommandeDto> create(@RequestBody CommandeDto dto) {
        CommandeDto created = service.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // CONFIRMER COMMANDE
    @PutMapping("/{id}/confirmer")
    public ResponseEntity<CommandeDto> confirmer(@PathVariable Integer id) {
        CommandeDto confirmed = service.confirmer(id);
        return ResponseEntity.ok(confirmed);
    }



}
