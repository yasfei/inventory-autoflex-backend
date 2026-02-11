package com.autoflex.inventory.resource;

import java.util.List;

import com.autoflex.inventory.dto.ProductRawMaterialDTO;
import com.autoflex.inventory.dto.ProductRawMaterialResponseDTO;
import com.autoflex.inventory.model.ProductRawMaterial;
import com.autoflex.inventory.service.ProductRawMaterialService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products/{productId}/raw-materials")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductRawMaterialResource {

    @Inject
    ProductRawMaterialService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addRawMaterial(
            @PathParam("productId") Long productId,
            ProductRawMaterialDTO dto) {

        ProductRawMaterial prm = service.addRawMaterial(
                productId,
                dto.rawMaterialId,
                dto.requiredQuantity);

        return Response.status(Response.Status.CREATED)
                .entity(ProductRawMaterialResponseDTO.fromEntity(prm))
                .build();

    }

    @GET
    public List<ProductRawMaterialResponseDTO> list(
            @PathParam("productId") Long productId) {

        return service.listByProduct(productId)
                .stream()
                .map(ProductRawMaterialResponseDTO::fromEntity)
                .toList();
    }

    @DELETE
    @Path("/{rawMaterialId}")
    @Transactional
    public Response remove(
            @PathParam("productId") Long productId,
            @PathParam("rawMaterialId") Long rawMaterialId) {

        service.remove(productId, rawMaterialId);
        return Response.noContent().build();
    }

}
