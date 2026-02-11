package com.autoflex.inventory.dto;

import com.autoflex.inventory.model.ProductRawMaterial;

public class ProductRawMaterialResponseDTO {

    public Long id;
    public Long rawMaterialId;
    public String rawMaterialName;
    public Integer requiredQuantity;

    public static ProductRawMaterialResponseDTO fromEntity(ProductRawMaterial prm) {
        ProductRawMaterialResponseDTO dto = new ProductRawMaterialResponseDTO();
        dto.id = prm.id;
        dto.rawMaterialId = prm.rawMaterial.id;
        dto.rawMaterialName = prm.rawMaterial.name;
        dto.requiredQuantity = prm.requiredQuantity;
        return dto;
    }
}
