package com.autoflex.inventory.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.autoflex.inventory.model.Product;
import com.autoflex.inventory.model.RawMaterial;
import com.autoflex.inventory.model.ProductRawMaterial;
import com.autoflex.inventory.repository.ProductRepository;
import com.autoflex.inventory.repository.RawMaterialRepository;

import javax.inject.Inject;

import java.math.BigDecimal;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class ProductionResourceTest {

    @Inject
    ProductRepository productRepository;

    @Inject
    RawMaterialRepository rawMaterialRepository;

    @BeforeEach
    public void setup() {
        // Limpa dados antes de cada teste
        productRepository.deleteAll();
        rawMaterialRepository.deleteAll();

        // Cria materiais
        RawMaterial rm1 = new RawMaterial();
        rm1.setCode("RM-001");
        rm1.setName("Aço");
        rm1.setQuantityInStock(100);
        rawMaterialRepository.persist(rm1);

        RawMaterial rm2 = new RawMaterial();
        rm2.setCode("RM-002");
        rm2.setName("Madeira");
        rm2.setQuantityInStock(50);
        rawMaterialRepository.persist(rm2);

        // Cria produtos
        Product p1 = new Product();
        p1.setCode("P-001");
        p1.setName("Produto A");
        p1.setValue(BigDecimal.valueOf(50));

        ProductRawMaterial prm1 = new ProductRawMaterial();
        prm1.setProduct(p1);
        prm1.setRawMaterial(rm1);
        prm1.setRequiredQuantity(10);

        p1.setRawMaterials(List.of(prm1));
        productRepository.persist(p1);

        Product p2 = new Product();
        p2.setCode("P-002");
        p2.setName("Produto B");
        p2.setValue(BigDecimal.valueOf(80));

        ProductRawMaterial prm2 = new ProductRawMaterial();
        prm2.setProduct(p2);
        prm2.setRawMaterial(rm1);
        prm2.setRequiredQuantity(20);

        ProductRawMaterial prm3 = new ProductRawMaterial();
        prm3.setProduct(p2);
        prm3.setRawMaterial(rm2);
        prm3.setRequiredQuantity(5);

        p2.setRawMaterials(List.of(prm2, prm3));
        productRepository.persist(p2);
    }

    @Test
    public void testCalculateProductionEndpoint() {
        given()
          .contentType(ContentType.JSON)
        .when()
          .get("/production")
        .then()
          .statusCode(200)
          .body("products.size()", greaterThan(0))
          .body("products[0].productName", notNullValue())
          .body("products[0].quantityProduced", notNullValue())
          .body("products[0].unitValue", notNullValue())
          .body("totalValue", notNullValue());
    }
}
