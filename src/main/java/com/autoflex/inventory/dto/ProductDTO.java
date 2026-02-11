package com.autoflex.inventory.dto;

import java.math.BigDecimal;
import java.util.List;

import com.autoflex.inventory.model.Product;

public class ProductDTO {

    public Long id;
    public String code;
    public String name;
    public BigDecimal value;
    public List<ProductRawMaterialDTO> rawMaterials;

   public static ProductDTO fromEntity(Product product) {
    ProductDTO dto = new ProductDTO();
    dto.id = product.id;
    dto.code = product.code;
    dto.name = product.name;
    dto.value = product.value;

    if (product.getRawMaterials() != null) {
        dto.rawMaterials = product.getRawMaterials().stream().map(prm -> {
            ProductRawMaterialDTO rmDto = new ProductRawMaterialDTO();
            rmDto.rawMaterialId = prm.getRawMaterial().id;
            rmDto.requiredQuantity = prm.getRequiredQuantity();
            return rmDto;
        }).toList();
    }

    return dto;
}

public Product toEntity() {
    Product product = new Product();
    product.code = this.code;
    product.name = this.name;
    product.value = this.value;
    return product;
}

    // Construtor padrão vazio
    public ProductDTO() {}

// Getters e setters para todos os campos
public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getCode() {
    return code;
}

public void setCode(String code) {
    this.code = code;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public BigDecimal getValue() {
    return value;
}

public void setValue(BigDecimal value) {
    this.value = value;
}

public List<ProductRawMaterialDTO> getRawMaterials() {
    return rawMaterials;
}

public void setRawMaterials(List<ProductRawMaterialDTO> rawMaterials) {
    this.rawMaterials = rawMaterials;
}
}
