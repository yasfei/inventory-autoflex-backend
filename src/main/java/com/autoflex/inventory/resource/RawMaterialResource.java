package com.autoflex.inventory.resource;

import java.util.List;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import com.autoflex.inventory.dto.RawMaterialDTO;
import com.autoflex.inventory.dto.RawMaterialRequest;
import com.autoflex.inventory.dto.RawMaterialResponse;
//import com.autoflex.inventory.model.Product;
import com.autoflex.inventory.model.ProductRawMaterial;
import com.autoflex.inventory.model.RawMaterial;
import com.autoflex.inventory.service.RawMaterialService;

//import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RawMaterialResource {

    @Inject
    RawMaterialService rawMaterialService;

    @GET
    public List<RawMaterial> getAll() {
        return rawMaterialService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        RawMaterial rawMaterial = rawMaterialService.findById(id);

        if (rawMaterial == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(rawMaterial).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createRawMaterial(@Valid RawMaterialDTO dto) {
        
        if (RawMaterial.find("code", dto.code).firstResult() != null) {
            throw new WebApplicationException("RawMaterial code already exists", 400);
        }
         
        RawMaterial rm = new RawMaterial();
        rm.code = dto.code;
        rm.name = dto.name;
        rm.quantityInStock = dto.quantityInStock;

        rm.persist();

        return Response.status(Response.Status.CREATED).entity(rm).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public RawMaterialResponse update(
            @PathParam("id") Long id,
            @Valid RawMaterialRequest request) {
        return rawMaterialService.update(id, request);
    }
    
@DELETE
@Path("/{id}")
@Transactional
public boolean delete(Long id) {
    RawMaterial material = RawMaterial.findById(id);
    if (material == null) {
        return false;
    }

    // Deleta todas as associações que usam essa RawMaterial
    ProductRawMaterial.delete("rawMaterial.id", id);

    // Agora podemos deletar a matéria-prima
    material.delete();
    return true;
}

}
