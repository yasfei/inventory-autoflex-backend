package com.autoflex.inventory.config;

import java.io.IOException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        // Frontend (5173) access backend
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:5173");

        // Permite os cabeçalhos que o frontend pode enviar
        responseContext.getHeaders().add("Access-Control-Allow-Headers", 
            "origin, content-type, accept, authorization");

        // Permite envio de cookies ou credenciais
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");

        // Permite todos os métodos que vamos usar
        responseContext.getHeaders().add("Access-Control-Allow-Methods", 
            "GET, POST, PUT, DELETE, OPTIONS, PATCH");

        // Define por quanto tempo o preflight é válido (em segundos)
        responseContext.getHeaders().add("Access-Control-Max-Age", "3600");
    }
}