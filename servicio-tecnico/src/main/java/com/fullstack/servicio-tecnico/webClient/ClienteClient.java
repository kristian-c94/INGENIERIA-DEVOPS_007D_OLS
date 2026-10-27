package com.fullstack.servicioTecnico.webClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Component
public class ClienteClient {

    private final WebClient webClient;


    public ClienteClient(@Value("${cliente-service.url}") String clienteUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(clienteUrl)
                .build();
    }

    public Map<String, Object> obtenerClienteId(Integer id, String token) {
        try {
            return this.webClient.get()
                    .uri("/{id}", id)
                    .header("Authorization", token)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError(),
                            response -> response.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("Cliente no encontrado en el sistema")))
                    .bodyToMono(Map.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar con el microservicio de Clientes: " + e.getMessage());
        }
    }
}