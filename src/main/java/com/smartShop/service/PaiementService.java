package com.smartShop.service;

import com.smartShop.dto.PaiementDto;
import com.smartShop.entity.Commande;
import com.smartShop.entity.Paiement;
import com.smartShop.enums.PaymentStatus;
import com.smartShop.enums.TypePaiement;
import com.smartShop.mapper.PaiementMapper;
import com.smartShop.repository.CommandeRepository;
import com.smartShop.repository.PaiementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final CommandeRepository commandeRepository;
    private final PaiementMapper mapper;

    private static final BigDecimal LIMITE_ESPECES = new BigDecimal("20000");

    // CRÉER PAIEMENT
    @Transactional
    public PaiementDto creerPaiement(PaiementDto dto) {
        Commande commande = commandeRepository.findById(dto.getCommandeId())
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        if (commande.getMontantRestant().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Commande déjà totalement payée");
        }

        if (dto.getTypePaiement() == TypePaiement.ESPECES && dto.getMontant().compareTo(LIMITE_ESPECES) > 0) {
            throw new RuntimeException("Paiement espèces dépasse la limite légale de 20,000 DH");
        }

        Paiement paiement = mapper.toEntity(dto);
        paiement.setCommande(commande);

        // Numéro séquentiel
        int numero = paiementRepository.countByCommande(commande) + 1;
        paiement.setNumeroPaiement(numero);

        LocalDateTime now = LocalDateTime.now();
        paiement.setDatePaiement(now);

        // Gestion de l'encaissement selon type
        if (dto.getTypePaiement() == TypePaiement.ESPECES || dto.getTypePaiement() == TypePaiement.VIREMENT) {
            paiement.setDateEncaissement(now);
            paiement.setStatusPaiement(PaymentStatus.ENCAISSÉ);
        } else { // CHÈQUE
            paiement.setDateEncaissement(null);
            paiement.setStatusPaiement(PaymentStatus.EN_ATTENTE);
        }

        Paiement saved = paiementRepository.save(paiement);

        // Mise à jour montant restant de la commande
        if (saved.getStatusPaiement() == PaymentStatus.ENCAISSÉ && saved.getMontant() != null) {
            BigDecimal nouveauRestant = commande.getMontantRestant().subtract(saved.getMontant());
            commande.setMontantRestant(nouveauRestant.max(BigDecimal.ZERO));
            commandeRepository.save(commande);
        }

        return mapper.toDTO(saved);
    }

    // MODIFIER STATUS / ENCAISSER
    @Transactional
    public PaiementDto mettreAJourStatus(Integer id, PaymentStatus status, LocalDateTime dateEncaissement) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        paiement.setStatusPaiement(status);

        if (status == PaymentStatus.ENCAISSÉ && paiement.getDateEncaissement() == null) {
            paiement.setDateEncaissement(dateEncaissement != null ? dateEncaissement : LocalDateTime.now());

            Commande cmd = paiement.getCommande();
            BigDecimal nouveauRestant = cmd.getMontantRestant().subtract(paiement.getMontant());
            cmd.setMontantRestant(nouveauRestant.max(BigDecimal.ZERO));
            commandeRepository.save(cmd);
        }

        return mapper.toDTO(paiementRepository.save(paiement));
    }


    // ANNULER PAIEMENT
    @Transactional
    public PaiementDto annulerPaiement(Integer id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));

        Commande cmd = paiement.getCommande();
        if (paiement.getStatusPaiement() == PaymentStatus.ENCAISSÉ) {
            cmd.setMontantRestant(cmd.getMontantRestant().add(paiement.getMontant()));
            commandeRepository.save(cmd);
        }

        paiementRepository.delete(paiement);
        return mapper.toDTO(paiement);
    }

    // GET ALL PAIEMENTS
    public List<PaiementDto> getAll() {
        return paiementRepository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // GET PAIEMENTS D'UNE COMMANDE
    public List<PaiementDto> getByCommande(Integer commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        return paiementRepository.findByCommandeOrderByNumeroPaiementAsc(commande).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // GET ONE PAIEMENT
    public PaiementDto getById(Integer id) {
        Paiement paiement = paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement introuvable"));
        return mapper.toDTO(paiement);
    }
}
