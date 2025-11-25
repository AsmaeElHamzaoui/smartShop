package com.smartShop.mapper;


import com.smartShop.dto.ProductDto;
import com.smartShop.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDTO(Product product);

    Product toEntity(ProductDto dto);
}

