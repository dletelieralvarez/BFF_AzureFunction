package com.biblioteca.bff.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EventGridProducerService {
    private final RestTemplate restTemplate;

    @Value("${azure.eventgrid.endpoint}")
    private String eventGridEndpoint;

    @Value("${azure.eventgrid.key}")
    private String eventGridKey;

    public EventGridProducerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void publicarPrestamoCreado(Integer libroId, Integer clienteId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("aeg-sas-key", eventGridKey);

        Map<String, Object> evento = Map.of(
                "id", UUID.randomUUID().toString(),
                "eventType", "PrestamoCreado",
                "subject", "biblioteca/prestamos",
                "eventTime", OffsetDateTime.now().toString(),
                "data", Map.of(
                        "mensaje", "Prestamo creado desde BFF",
                        "libroId", libroId,
                        "clienteId", clienteId
                ),
                "dataVersion", "1.0"
        );

        HttpEntity<List<Map<String, Object>>> entity =
                new HttpEntity<>(List.of(evento), headers);

        restTemplate.exchange(
                eventGridEndpoint,
                HttpMethod.POST,
                entity,
                String.class
        );
    }
}