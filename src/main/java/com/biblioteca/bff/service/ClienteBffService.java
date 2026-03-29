package com.biblioteca.bff.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.biblioteca.bff.dto.ClienteDto;

@Service
public class ClienteBffService {

    private final RestTemplate restTemplate;

    @Value("${azure.functions.clientes.url}")
    private String funClientes;

    public ClienteBffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> obtenerClientes() {
        try {
            ResponseEntity<List<ClienteDto>> response = restTemplate.exchange(
                    funClientes,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ClienteDto>>() {}
            );

            List<ClienteDto> body = response.getBody();

            return ResponseEntity
                    .status(response.getStatusCode())
                    .body(body != null ? body : Collections.emptyList());

        } catch (RestClientResponseException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getResponseBodyAsString());
        }
    }

    public ResponseEntity<?> obtenerClientePorId(Integer id) {
        try {
            String url = funClientes + "/" + id;

            ResponseEntity<ClienteDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    ClienteDto.class
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

    public ResponseEntity<?> crearCliente(ClienteDto request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ClienteDto> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ClienteDto> response = restTemplate.exchange(
                    funClientes,
                    HttpMethod.POST,
                    entity,
                    ClienteDto.class
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

    public ResponseEntity<?> actualizarCliente(Integer id, ClienteDto request) {
        try {
            ClienteDto body = new ClienteDto();
            body.setId(id);
            body.setRut(request.getRut());
            body.setDv(request.getDv());
            body.setNombre(request.getNombre());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ClienteDto> entity = new HttpEntity<>(body, headers);

            String url = funClientes + "/" + id;

            ResponseEntity<ClienteDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    ClienteDto.class
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

    public ResponseEntity<?> eliminarCliente(Integer id) {
        try {
            String url = funClientes + "/" + id;

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    String.class
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
}