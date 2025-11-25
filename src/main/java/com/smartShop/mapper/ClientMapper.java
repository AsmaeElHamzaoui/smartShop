package com.smartShop.mapper;

import com.smartShop.dto.ClientDto;
import com.smartShop.entity.Client;
import com.smartShop.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(source = "user.id", target = "userId")
    ClientDto toDTO(Client client);

    @Mapping(source = "userId", target = "user", qualifiedByName = "mapUserIdToUser")
    Client toEntity(ClientDto dto);


    //default methode pour mapper UserId to User
    @Named("mapUserIdToUser")
    default User mapUserIdToUser(Integer userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

}
