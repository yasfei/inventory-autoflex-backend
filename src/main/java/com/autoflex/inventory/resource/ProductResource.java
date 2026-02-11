package com.autoflex.inventory.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.autoflex.inventory.dto.ProductDTO;
import com.autoflex.inventory.dto.ProductRawMaterialDTO;
import com.autoflex.inventory.model.Product;
import com.autoflex.inventory.model.ProductRawMaterial;
import com.autoflex.inventory.model.RawMaterial;
import com.autoflex.inventory.service.ProductService;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    public List<ProductDTO> getAll() {
        return Product.listAll().stream()
                .map(p -> ProductDTO.fromEntity((Product) p))
                .toList();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Product product = Product.findById(id);

        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(ProductDTO.fromEntity(product)).build();
    }

@POST
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public Response create(ProductDTO dto) {
    if (dto == null) {
        throw new RuntimeException("DTO is null!");
    }

    // Verifica se já existe um produto com mesmo code
    Product existing = Product.find("code", dto.getCode()).firstResult();
    if (existing != null) {
        return Response.status(Response.Status.CONFLICT)
                       .entity(Map.of("error", "Product code already exists"))
                       .build();
    }

    Product product = dto.toEntity();
    product.persist();

    if (dto.rawMaterials != null) {
        for (ProductRawMaterialDTO rmDto : dto.rawMaterials) {
            RawMaterial rm = RawMaterial.findById(rmDto.rawMaterialId);

            ProductRawMaterial prm = new ProductRawMaterial();
            prm.setProduct(product);
            prm.setRawMaterial(rm);
            prm.setRequiredQuantity(rmDto.requiredQuantity);

            if (product.getRawMaterials() == null) {
                product.setRawMaterials(new ArrayList<>()); // <-- importante!
            }
            product.getRawMaterials().add(prm);
        }
    }

    return Response.ok(ProductDTO.fromEntity(product)).build();
}


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response update(@PathParam("id") Long id, ProductDTO dto) {
        System.out.println("Received DTO: " + dto);
        Product product = productService.updateFromDTO(id, dto);

        if (product == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(ProductDTO.fromEntity(product)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        boolean deleted = productService.delete(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

}
