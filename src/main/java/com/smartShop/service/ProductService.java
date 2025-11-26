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

    // READ ALL
    public List<ProductDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // UPDATE
    public ProductDto update(Integer id, ProductDto dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + id));

        existing.setNom(dto.getNom());
        existing.setPrixUnitaire(dto.getPrixUnitaire());
        existing.setStockDisponible(dto.getStockDisponible());

        Product updated = productRepository.save(existing);
        return productMapper.toDTO(updated);
    }


    // DELETE
    public void delete(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produit introuvable : " + id);
        }
        productRepository.deleteById(id);
    }
}
