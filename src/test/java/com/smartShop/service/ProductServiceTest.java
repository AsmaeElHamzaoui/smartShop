package com.smartShop.service;

import com.smartShop.dto.ProductDto;
import com.smartShop.entity.Product;
import com.smartShop.mapper.ProductMapper;
import com.smartShop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1);
        product.setNom("Produit1");
        product.setPrixUnitaire(BigDecimal.valueOf(100));
        product.setStockDisponible(10);
        product.setDeleted(false);

        productDto = new ProductDto();
        productDto.setId(1);
        productDto.setNom("Produit1");
        productDto.setPrixUnitaire(BigDecimal.valueOf(100));
        productDto.setStockDisponible(10);
    }

    @Test
    void testCreate() {
        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(productDto);

        ProductDto result = productService.create(productDto);

        assertEquals(productDto.getNom(), result.getNom());
        verify(productRepository, times(1)).save(product);
        verify(productMapper, times(1)).toEntity(productDto);
        verify(productMapper, times(1)).toDTO(product);
    }

    @Test
    void testGetById_ProductExists() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(productDto);

        ProductDto result = productService.getById(1);

        assertEquals(productDto.getNom(), result.getNom());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testGetById_ProductDeleted() {
        product.setDeleted(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getById(1));
        assertEquals("Produit non trouvé : 1", exception.getMessage());
    }

    @Test
    void testGetById_ProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.getById(1));
        assertEquals("Produit non trouvé : 1", exception.getMessage());
    }

    @Test
    void testGetAll() {
        Product deletedProduct = new Product();
        deletedProduct.setDeleted(true);

        when(productRepository.findAll()).thenReturn(Arrays.asList(product, deletedProduct));
        when(productMapper.toDTO(product)).thenReturn(productDto);

        List<ProductDto> result = productService.getAll();

        assertEquals(1, result.size());
        assertEquals(productDto.getNom(), result.get(0).getNom());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdate_ProductExists() {
        ProductDto updateDto = new ProductDto();
        updateDto.setNom("UpdatedName");
        updateDto.setPrixUnitaire(BigDecimal.valueOf(200));
        updateDto.setStockDisponible(5);

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(updateDto);

        ProductDto result = productService.update(1, updateDto);

        assertEquals("UpdatedName", result.getNom());
        assertEquals(BigDecimal.valueOf(200), result.getPrixUnitaire());
        assertEquals(5, result.getStockDisponible());
    }

    @Test
    void testUpdate_ProductDeleted() {
        product.setDeleted(true);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.update(1, productDto));
        assertEquals("Impossible de modifier un produit supprimé", exception.getMessage());
    }

    @Test
    void testUpdate_ProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productService.update(1, productDto));
        assertEquals("Produit non trouvé : 1", exception.getMessage());
    }

    @Test
    void testDelete_ProductExists() {
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productService.delete(1);

        assertTrue(product.isDeleted());
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    void testDelete_ProductNotFound() {
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.delete(1));
        assertEquals("Produit introuvable : 1", exception.getMessage());
    }
}
