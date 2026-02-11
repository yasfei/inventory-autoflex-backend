package com.autoflex.inventory.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.autoflex.inventory.dto.ProducedProductDTO;
import com.autoflex.inventory.dto.ProductionResultDTO;
import com.autoflex.inventory.model.Product;
import com.autoflex.inventory.model.ProductRawMaterial;
import com.autoflex.inventory.model.RawMaterial;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProductionService {

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @Transactional
    public ProductionResultDTO calculateProduction() {

        // 1. Buscar estoque atual (Maior valor > menor valor, subtraindo do estoque)
        Map<Long, Integer> stock = rawMaterialRepository.findAll()
                .list()
                .stream()
                .collect(Collectors.toMap(
                        RawMaterial::getId,
                        RawMaterial::getQuantityInStock));

        // Nota: Estou utilizando um método em que a prodção é feita primeiro com os
        // produtos mais valiosos.
        // Se quiséssemos mostrar o potencial máximo de produção sem consumir estoque,
        // poderíamos calcular
        // todas as quantidades possíveis de cada produto sem atualizar o estoque,
        // usando uma cópia do estoque.
        // Isso garantiria que todos os produtos viáveis aparecessem na simulação (fair
        // share).

        // 2. Buscar produtos ordenados por MAIOR valor
        List<Product> products = productRepository
                .find("ORDER BY value DESC")
                .list();

        List<ProducedProductDTO> produced = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // 3. Para cada produto
        for (Product product : products) {

            // Ignora produtos sem matérias-primas
            if (product.getRawMaterials() == null || product.getRawMaterials().isEmpty()) {
                continue;
            }

            int maxPossible = Integer.MAX_VALUE; // limitante de produção

            // 4. Ver limitante de cada matéria-prima
            for (ProductRawMaterial prm : product.getRawMaterials()) {

                if (prm.getRequiredQuantity() <= 0) {
                    maxPossible = 0;
                    break;
                }

                int available = stock.getOrDefault(
                        prm.getRawMaterial().getId(), 0);

                int possibleByMaterial = available / prm.getRequiredQuantity();
                maxPossible = Math.min(maxPossible, possibleByMaterial);
            }

            // // 5. Se não pode produzir, pula
            // if (maxPossible <= 0)
            // continue;

            // 5. Consumir estoque apenas se há produção
            if (maxPossible > 0) {
                for (ProductRawMaterial prm : product.getRawMaterials()) {
                    Long rmId = prm.getRawMaterial().getId();
                    int used = prm.getRequiredQuantity() * maxPossible;
                    stock.put(rmId, stock.get(rmId) - used);
                }

            }

            // 7. Montar DTO se maxPossible == 0
            ProducedProductDTO dto = new ProducedProductDTO();
            dto.productId = product.getId();
            dto.productCode = product.getCode();
            dto.productName = product.getName();
            dto.unitValue = product.getValue();
            dto.quantityProduced = maxPossible;
            dto.totalValue = product.getValue().multiply(BigDecimal.valueOf(maxPossible));

            produced.add(dto);
            total = total.add(dto.totalValue);
        }

        ProductionResultDTO result = new ProductionResultDTO();
        result.products = produced;
        result.totalValue = total;

        return result;
    }
}
