package com.smartShop.mapper;

import com.smartShop.dto.CommandeDto;
import com.smartShop.entity.Client;
import com.smartShop.entity.Commande;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CommandeMapper {

    @Mapping(source = "client.id", target = "clientId")
    CommandeDto toDTO(Commande entity);

    @Mapping(source = "clientId", target = "client.id")
    Commande toEntity(CommandeDto dto);
}
