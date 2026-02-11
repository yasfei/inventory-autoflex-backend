package com.autoflex.inventory.resource;


import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class RawMaterialResourceTest {

    @Test
    void shouldCreateRawMaterial() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                          {
                            "code": "RM-TESTE1",
                            "name": "Material Teste",
                            "quantityInStock": 100
                          }
                        """)
                .when()
                .post("/raw-materials")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldFailWhenNameIsMissing() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                          {
                            "code": "RM-TESTE2",
                            "name": "Material Teste2",
                            "quantityInStock": -10
                          }
                        """)
                .when()
                .post("/raw-materials")
                .then()
                .statusCode(400);
    }

}
