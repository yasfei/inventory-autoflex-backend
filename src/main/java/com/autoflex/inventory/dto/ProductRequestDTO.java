package com.autoflex.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductRequestDTO {

    @NotBlank
    public String name;

    @NotNull
    public BigDecimal value;

}
