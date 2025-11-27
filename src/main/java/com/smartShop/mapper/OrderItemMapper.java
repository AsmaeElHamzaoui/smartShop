package com.smartShop.mapper;

import com.smartShop.dto.OrderItemDto;
import com.smartShop.entity.Commande;
import com.smartShop.entity.OrderItem;
import com.smartShop.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "commande.id", target = "commandeId")
    OrderItemDto toDTO(OrderItem entity);

    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "commandeId", target = "commande.id")
    OrderItem toEntity(OrderItemDto dto);
}
