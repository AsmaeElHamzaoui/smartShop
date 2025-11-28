package com.smartShop.mapper;

import com.smartShop.dto.PaiementDto;
import com.smartShop.entity.Paiement;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaiementMapper {

    @Mapping(source = "commande.id", target = "commandeId")
    PaiementDto toDTO(Paiement paiement);

    @Mapping(target = "commande", ignore = true)
    @Mapping(target = "numeroPaiement", ignore = true)
    @Mapping(target = "datePaiement", ignore = true)
    @Mapping(target = "dateEncaissement", ignore = true)
    Paiement toEntity(PaiementDto dto);
}
