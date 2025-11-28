package com.smartShop.controller;

import com.smartShop.dto.PaiementDto;
import com.smartShop.enums.PaymentStatus;
import com.smartShop.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/paiements")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService service;

    @PostMapping
    public ResponseEntity<PaiementDto> creerPaiement(@RequestBody PaiementDto dto) {
        return ResponseEntity.ok(service.creerPaiement(dto));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PaiementDto> mettreAJourStatus(
            @PathVariable Integer id,
            @RequestParam PaymentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateEncaissement
    ) {
        return ResponseEntity.ok(service.mettreAJourStatus(id, status, dateEncaissement));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaiementDto> annulerPaiement(@PathVariable Integer id) {
        return ResponseEntity.ok(service.annulerPaiement(id));
    }

    @GetMapping
    public ResponseEntity<List<PaiementDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaiementDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<List<PaiementDto>> getByCommande(@PathVariable Integer commandeId) {
        return ResponseEntity.ok(service.getByCommande(commandeId));
    }
}
