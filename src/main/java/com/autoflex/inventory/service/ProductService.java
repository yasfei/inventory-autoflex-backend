// Service layer for Product
// Handles business logic and persistence
// Uses Panache entity Product

package com.autoflex.inventory.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import java.util.List;

import com.autoflex.inventory.dto.ProductDTO;
import com.autoflex.inventory.dto.ProductRawMaterialDTO;
import com.autoflex.inventory.model.Product;
import com.autoflex.inventory.model.ProductRawMaterial;
import com.autoflex.inventory.model.RawMaterial;

@ApplicationScoped
public class ProductService {

    public List<Product> findAll() {
        return Product.listAll();
    }

    public Product findById(Long id) {
        return Product.findById(id);
    }

    @Transactional
    public Product createFromDTO(ProductDTO dto) {

        Product product = new Product();
        product.code = dto.code;
        product.name = dto.name;
        product.value = dto.value;

        product.persist(); // precisa do ID antes

        applyRawMaterials(product, dto.rawMaterials);

        return product;
    }

    @Transactional
    public Product updateFromDTO(Long id, ProductDTO dto) {

        Product product = Product.findById(id);
        if (product == null) return null;

        product.code = dto.code;
        product.name = dto.name;
        product.value = dto.value;

        product.getRawMaterials().clear(); // orphanRemoval cuida do delete

        applyRawMaterials(product, dto.rawMaterials);

        return product;
    }

    private void applyRawMaterials(
        Product product,
        List<ProductRawMaterialDTO> rawMaterials
    ) {
        if (rawMaterials == null) return;

        for (ProductRawMaterialDTO rmDto : rawMaterials) {

            RawMaterial rm = RawMaterial.findById(rmDto.rawMaterialId);
            if (rm == null) {
                throw new WebApplicationException(
                    "Raw material not found: " + rmDto.rawMaterialId, 400
                );
            }

            ProductRawMaterial prm = new ProductRawMaterial();
            prm.setProduct(product);
            prm.setRawMaterial(rm);
            prm.setRequiredQuantity(rmDto.requiredQuantity);

            product.getRawMaterials().add(prm);
        }
    }

    @Transactional
    public boolean delete(Long id) {
        return Product.deleteById(id);
    }
}
