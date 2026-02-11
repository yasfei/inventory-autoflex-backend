package com.autoflex.inventory.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;


import com.autoflex.inventory.dto.ProductDTO;

import static io.restassured.RestAssured.given;

import java.math.BigDecimal;

@QuarkusTest
public class ProductResourceTest {

    @Test
    public void testCreateProduct() {
        ProductDTO dto = new ProductDTO();
        dto.code = "P-TESTE";
        dto.name = "Produto Teste";
        dto.value = new BigDecimal("10.50");

        given()
          .contentType(ContentType.JSON)
          .body(dto)
          .when().post("/products")
          .then()
             .statusCode(201)
             .body("id", notNullValue())
             .body("code", equalTo("P-TESTE"))
             .body("name", equalTo("Produto Teste"))
             .body("value", equalTo(10.50f));
    }

    @Test
    public void testFindAll() {
        given()
          .when().get("/products")
          .then()
             .statusCode(200);
    }
}

