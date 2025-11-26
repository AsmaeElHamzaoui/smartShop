package com.smartShop.service;

import com.smartShop.dto.ProductDto;
import com.smartShop.entity.Product;
import com.smartShop.mapper.ProductMapper;
import com.smartShop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    // CREATE
    public ProductDto create(ProductDto dto) {
        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    // READ ONE
    public ProductDto getById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + id));
        return productMapper.toDTO(product);
    }
}
