package com.biblioteca.bff.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.biblioteca.bff.dto.LibroDto;
import com.biblioteca.bff.dto.PrestamoDto;

@Service
public class EventGridProducerService {
    private final RestTemplate restTemplate;

    @Value("${azure.functions.prestamos.url}")
    private String funPrestamos;

    @Value("${azure.functions.libros.url}")
    private String funLibros;

    @Value("${azure.eventgrid.endpoint}")
    private String eventGridEndpoint;

    @Value("${azure.eventgrid.key}")
    private String eventGridKey;

    public EventGridProducerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    //llama a Azure Function para crear el préstamo
    //envia los datos del prestamo
    //guarda en la BD
    public ResponseEntity<?> crearPrestamo(PrestamoDto request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PrestamoDto> entity = new HttpEntity<>(request, headers);

            ResponseEntity<PrestamoDto> response = restTemplate.exchange(
                    funPrestamos,
                    HttpMethod.POST,
                    entity,
                    PrestamoDto.class
            );

            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(response.getBody());

        } catch (RestClientResponseException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getResponseBodyAsString());
        }
    }

    //construye un evento json
    //lo envia a azure event grid
    //event grid lo distribuye al consumidor
    //el json lo recibe el consumidor y lo procesa
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

    //llama a Azure Function para crear libro
    //envia los datos del libro
    //guarda en la BD
    public ResponseEntity<?> crearLibro(LibroDto request) {
        try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<LibroDto> entity = new HttpEntity<>(request, headers);

                ResponseEntity<LibroDto> response = restTemplate.exchange(
                        funLibros,
                        HttpMethod.POST,
                        entity,
                        LibroDto.class
                );

                return ResponseEntity
                        .status(response.getStatusCode())
                        .body(response.getBody());

        } catch (RestClientResponseException e) {
                return ResponseEntity
                        .status(e.getStatusCode())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e.getResponseBodyAsString());
        }
    }


    /*PUBLICAR LIBRO CREADO */
    //construye un evento json
    //lo envia a azure event grid
    //event grid lo distribuye al consumidor
    //el json lo recibe el consumidor y lo procesa
    public void publicarLibroCreado(Integer libroId, String titulo, String autor) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("aeg-sas-key", eventGridKey);

        Map<String, Object> evento = Map.of(
                "id", UUID.randomUUID().toString(),
                "eventType", "LibroCreado",
                "subject", "biblioteca/libros",
                "eventTime", OffsetDateTime.now().toString(),
                "data", Map.of(
                        "mensaje", "Libro creado desde BFF",
                        "libroId", libroId,
                        "titulo", titulo,
                        "autor", autor
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