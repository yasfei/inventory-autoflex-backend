// Service layer for RawMaterial
// Handles business logic and persistence
// Same CRUD pattern used in ProductService
package com.autoflex.inventory.service;

import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import com.autoflex.inventory.dto.RawMaterialRequest;
import com.autoflex.inventory.dto.RawMaterialResponse;
import com.autoflex.inventory.model.RawMaterial;

@ApplicationScoped
public class RawMaterialService {

    public List<RawMaterial> findAll() {
        return RawMaterial.listAll();
    }

    public RawMaterial findById(Long id) {
        return RawMaterial.findById(id);
    }

    @Transactional
    public RawMaterialResponse create(RawMaterialRequest request) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.code = request.code;
        rawMaterial.name = request.name;
        rawMaterial.quantityInStock = request.quantityInStock;

        rawMaterial.persist();

        return toResponse(rawMaterial);
    }

    @Transactional
    public RawMaterialResponse update(Long id, RawMaterialRequest request) {
        RawMaterial rawMaterial = RawMaterial.findById(id);

        if (rawMaterial == null) {
            throw new NotFoundException();
        }

        rawMaterial.code = request.code;
        rawMaterial.name = request.name;
        rawMaterial.quantityInStock = request.quantityInStock;

        return toResponse(rawMaterial);
    }

    @Transactional
    public boolean delete(Long id) {
        return RawMaterial.deleteById(id);
    }

    private RawMaterialResponse toResponse(RawMaterial rawMaterial) {
        RawMaterialResponse response = new RawMaterialResponse();
        response.id = rawMaterial.id;
        response.code = rawMaterial.code;
        response.name = rawMaterial.name;
        response.quantityInStock = rawMaterial.quantityInStock;
        return response;
    }
}
