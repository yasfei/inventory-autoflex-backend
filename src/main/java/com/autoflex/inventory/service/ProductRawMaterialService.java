package com.autoflex.inventory.service;

import com.autoflex.inventory.model.Product;
import com.autoflex.inventory.model.ProductRawMaterial;
import com.autoflex.inventory.model.RawMaterial;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class ProductRawMaterialService {

    @Transactional
    public ProductRawMaterial addRawMaterial(
            Long productId,
            Long rawMaterialId,
            Integer requiredQuantity) {

        Product product = Product.findById(productId);
        if (product == null) {
            throw new NotFoundException("Product not found");
        }

        RawMaterial rawMaterial = RawMaterial.findById(rawMaterialId);
        if (rawMaterial == null) {
            throw new NotFoundException("Raw material not found");
        }

        ProductRawMaterial prm = new ProductRawMaterial();
        prm.product = product;
        prm.rawMaterial = rawMaterial;
        prm.requiredQuantity = requiredQuantity;
        prm.persist();

        return prm;
    }

    // 🔹 GET
    public List<ProductRawMaterial> listByProduct(Long productId) {
        return ProductRawMaterial.list("product.id", productId);
    }

    // 🔹 DELETE
    @Transactional
    public void remove(Long productId, Long rawMaterialId) {
        ProductRawMaterial prm = ProductRawMaterial.find(
                "product.id = ?1 and rawMaterial.id = ?2",
                productId,
                rawMaterialId).firstResult();

        if (prm == null) {
            throw new NotFoundException("Relation not found");
        }

        prm.delete();
    }
}
