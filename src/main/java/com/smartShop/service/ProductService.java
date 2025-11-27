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
                .filter(p -> !p.isDeleted()) // exclure les produits supprimés
                .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + id));
        return productMapper.toDTO(product);
    }

    // READ ALL
    public List<ProductDto> getAll() {
        return productRepository.findAll()
                .stream()
                .filter(p -> !p.isDeleted()) // exclure les produits supprimés
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    // UPDATE
    public ProductDto update(Integer id, ProductDto dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé : " + id));

       // empêcher la modification d’un produit supprimé
        if (existing.isDeleted()) {
            throw new RuntimeException("Impossible de modifier un produit supprimé");
        }

        existing.setNom(dto.getNom());
        existing.setPrixUnitaire(dto.getPrixUnitaire());
        existing.setStockDisponible(dto.getStockDisponible());

        Product updated = productRepository.save(existing);
        return productMapper.toDTO(updated);
    }


    // DELETE
    public void delete(Integer id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable : " + id));

        existing.setDeleted(true); // soft delete
        productRepository.deleteById(id);
    }
}
