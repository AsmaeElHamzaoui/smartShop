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


}
