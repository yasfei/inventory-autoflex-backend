package com.autoflex.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import com.autoflex.inventory.model.ProductRawMaterial;

public class ProductRawMaterialDTO {

    @NotNull
    public Long rawMaterialId;

    @NotNull
    @Min(1)
    public Integer requiredQuantity;


    public static ProductRawMaterialDTO fromEntity(ProductRawMaterial prm) {
        ProductRawMaterialDTO dto = new ProductRawMaterialDTO();
        dto.rawMaterialId = prm.getRawMaterial().getId();
        dto.requiredQuantity = prm.getRequiredQuantity();
        return dto;
    }
}
