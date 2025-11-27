package com.smartShop.controller;
import com.smartShop.dto.CommandeDto;
import com.smartShop.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    // CREATE
    @PostMapping
    public ResponseEntity<CommandeDto> create(@RequestBody CommandeDto dto) {
        return ResponseEntity.ok(commandeService.create(dto));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<CommandeDto>> getAll() {
        return ResponseEntity.ok(commandeService.getAll());
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<CommandeDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(commandeService.getById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<CommandeDto> update(
            @PathVariable Integer id,
            @RequestBody CommandeDto dto
    ) {
        return ResponseEntity.ok(commandeService.update(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        commandeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
