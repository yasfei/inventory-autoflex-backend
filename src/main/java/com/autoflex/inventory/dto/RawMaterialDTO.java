package com.autoflex.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RawMaterialDTO {

    @NotBlank
    public String code;

    @NotBlank
    public String name;

    @NotNull
    @Min(0)
    public Integer quantityInStock;
}
