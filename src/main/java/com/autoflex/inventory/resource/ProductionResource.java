package com.autoflex.inventory.resource;

import com.autoflex.inventory.dto.ProductionResultDTO;
import com.autoflex.inventory.service.ProductionService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/production")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductionResource {

    @Inject
    ProductionService productionService;

    @GET
    public ProductionResultDTO calculateProduction() {
        return productionService.calculateProduction();
    }

}
