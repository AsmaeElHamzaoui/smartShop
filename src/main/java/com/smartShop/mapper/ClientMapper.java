package com.smartShop.mapper;

import com.smartShop.dto.ClientDto;
import com.smartShop.entity.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDto toDTO(Client client);
    Client toEntity(ClientDto dto);
}
