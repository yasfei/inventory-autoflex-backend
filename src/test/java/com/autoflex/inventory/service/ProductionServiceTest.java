package com.autoflex.inventory.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.autoflex.inventory.dto.ProducedProductDTO;
import com.autoflex.inventory.dto.ProductionResultDTO;
import com.autoflex.inventory.model.Product;
import com.autoflex.inventory.model.ProductRawMaterial;
import com.autoflex.inventory.model.RawMaterial;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

class ProductionServiceTest {

    ProductionService service;

    ProductRepository productRepository;
    RawMaterialRepository rawMaterialRepository;

    @BeforeEach
    void setup() {
        service = new ProductionService();

        // Mocks dos repositórios
        productRepository = mock(ProductRepository.class);
        rawMaterialRepository = mock(RawMaterialRepository.class);

        // Injeta os mocks no serviço
        service.productRepository = productRepository;
        service.rawMaterialRepository = rawMaterialRepository;
    }

    @Test
    void testCalculateProductionSingleProduct() {
        // --- Matérias-primas ---
        RawMaterial rm1 = new RawMaterial();
        rm1.setId(1L);
        rm1.setName("Aço");
        rm1.setQuantityInStock(100);

        RawMaterial rm2 = new RawMaterial();
        rm2.setId(2L);
        rm2.setName("Cobre");
        rm2.setQuantityInStock(50);

        // Mock PanacheQuery para matérias-primas
        @SuppressWarnings("unchecked")
        PanacheQuery<RawMaterial> rawQueryMock = mock(PanacheQuery.class);
        when(rawQueryMock.list()).thenReturn(Arrays.asList(rm1, rm2));
        when(rawMaterialRepository.findAll()).thenReturn(rawQueryMock);

        // --- Produto ---
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Produto A");
        p1.setCode("P-001");
        p1.setValue(BigDecimal.valueOf(10));

        ProductRawMaterial prm1 = new ProductRawMaterial();
        prm1.setRawMaterial(rm1);
        prm1.setRequiredQuantity(2);

        ProductRawMaterial prm2 = new ProductRawMaterial();
        prm2.setRawMaterial(rm2);
        prm2.setRequiredQuantity(1);

        p1.setRawMaterials(Arrays.asList(prm1, prm2));

        // Mock PanacheQuery para produtos
        @SuppressWarnings("unchecked")
        PanacheQuery<Product> productQueryMock = mock(PanacheQuery.class);
        when(productQueryMock.list()).thenReturn(List.of(p1));
        when(productRepository.find("ORDER BY value DESC")).thenReturn(productQueryMock);

        // --- Executa ---
        ProductionResultDTO result = service.calculateProduction();

        // --- Verifica ---
        assertNotNull(result);
        assertEquals(1, result.products.size());

        ProducedProductDTO produced = result.products.get(0);
        assertEquals("P-001", produced.productCode);
        assertEquals("Produto A", produced.productName);
        assertEquals(BigDecimal.valueOf(10), produced.unitValue);
        assertEquals(50, produced.quantityProduced); // calculado pelo limite da matéria-prima
        assertEquals(BigDecimal.valueOf(500), produced.totalValue); // 50 * 10
    }

    @Test
    void testCalculateProductionMultipleProducts() {
        // --- Matérias-primas ---
        RawMaterial rm1 = new RawMaterial();
        rm1.setId(1L);
        rm1.setName("Aço");
        rm1.setQuantityInStock(10);

        RawMaterial rm2 = new RawMaterial();
        rm2.setId(2L);
        rm2.setName("Cobre");
        rm2.setQuantityInStock(5);

        @SuppressWarnings("unchecked")
        PanacheQuery<RawMaterial> rawQueryMock = mock(PanacheQuery.class);
        when(rawQueryMock.list()).thenReturn(Arrays.asList(rm1, rm2));
        when(rawMaterialRepository.findAll()).thenReturn(rawQueryMock);

        // --- Produto mais valioso ---
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Produto Valioso");
        p1.setCode("P-VAL");
        p1.setValue(BigDecimal.valueOf(20));

        ProductRawMaterial prm1 = new ProductRawMaterial();
        prm1.setRawMaterial(rm1);
        prm1.setRequiredQuantity(5); // 10/5 = 2
        p1.setRawMaterials(List.of(prm1));

        // --- Produto menos valioso ---
        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Produto Comum");
        p2.setCode("P-COM");
        p2.setValue(BigDecimal.valueOf(10));

        ProductRawMaterial prm2 = new ProductRawMaterial();
        prm2.setRawMaterial(rm1);
        prm2.setRequiredQuantity(3);

        ProductRawMaterial prm3 = new ProductRawMaterial();
        prm3.setRawMaterial(rm2);
        prm3.setRequiredQuantity(2);

        p2.setRawMaterials(Arrays.asList(prm2, prm3));

        @SuppressWarnings("unchecked")
        PanacheQuery<Product> productQueryMock = mock(PanacheQuery.class);
        when(productQueryMock.list()).thenReturn(Arrays.asList(p1, p2));
        when(productRepository.find("ORDER BY value DESC")).thenReturn(productQueryMock);

        // --- Executa ---
        ProductionResultDTO result = service.calculateProduction();

        // --- Verifica ---
        assertNotNull(result);
        assertEquals(2, result.products.size());

        ProducedProductDTO prod1 = result.products.get(0);
        assertEquals("P-VAL", prod1.productCode);
        assertEquals(2, prod1.quantityProduced); // limitado pelo estoque do rm1
        assertEquals(BigDecimal.valueOf(40), prod1.totalValue);

        ProducedProductDTO prod2 = result.products.get(1);
        assertEquals("P-COM", prod2.productCode);
        assertEquals(0, prod2.quantityProduced); // estoque restante não permite produção
    }

    @Test
    void testCalculateProductionEmptyStock() {
        @SuppressWarnings("unchecked")
        PanacheQuery<RawMaterial> rawQueryMock = mock(PanacheQuery.class);
        when(rawQueryMock.list()).thenReturn(List.of());
        when(rawMaterialRepository.findAll()).thenReturn(rawQueryMock);

        @SuppressWarnings("unchecked")
        PanacheQuery<Product> productQueryMock = mock(PanacheQuery.class);
        when(productQueryMock.list()).thenReturn(List.of());
        when(productRepository.find("ORDER BY value DESC")).thenReturn(productQueryMock);

        ProductionResultDTO result = service.calculateProduction();
        assertNotNull(result);
        assertTrue(result.products.isEmpty());
        assertEquals(BigDecimal.ZERO, result.totalValue);
    }
}
