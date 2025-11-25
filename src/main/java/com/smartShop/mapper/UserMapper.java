package com.smartShop.mapper;

import com.smartShop.dto.UserDto;
import com.smartShop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDTO(User user);

    User toEntity(UserDto dto);
}
