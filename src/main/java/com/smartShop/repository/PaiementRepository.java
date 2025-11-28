package com.smartShop.repository;

import com.smartShop.entity.Paiement;
import com.smartShop.entity.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {

    List<Paiement> findByCommandeOrderByNumeroPaiementAsc(Commande commande);

    Integer countByCommande(Commande commande);
}
