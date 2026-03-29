package com.biblioteca.bff.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.biblioteca.bff.dto.UsuarioDto;

@Service
public class UsuarioBffService {

    private final RestTemplate restTemplate;

    @Value("${azure.functions.usuarios.usuarios.url}")
    private String funUsuarios;

    public UsuarioBffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> obtenerUsuarios() {
        try {
            ResponseEntity<List<UsuarioDto>> response = restTemplate.exchange(
                    funUsuarios,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<UsuarioDto>>() {}
            );

            List<UsuarioDto> body = response.getBody();

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

    public ResponseEntity<?> obtenerUsuarioPorId(Integer id) {
        try {
            String url = funUsuarios + "/" + id;

            ResponseEntity<UsuarioDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    UsuarioDto.class
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

    public ResponseEntity<?> crearUsuario(UsuarioDto request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioDto> entity = new HttpEntity<>(request, headers);

            ResponseEntity<UsuarioDto> response = restTemplate.exchange(
                    funUsuarios,
                    HttpMethod.POST,
                    entity,
                    UsuarioDto.class
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

    public ResponseEntity<?> actualizarUsuario(Integer id, UsuarioDto request) {
        try {
            UsuarioDto body = new UsuarioDto();
            body.setId(id);
            body.setNombre(request.getNombre());
            body.setEmail(request.getEmail());
            body.setRol(request.getRol());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<UsuarioDto> entity = new HttpEntity<>(body, headers);

            String url = funUsuarios + "/" + id;

            ResponseEntity<UsuarioDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    UsuarioDto.class
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

    public ResponseEntity<?> eliminarUsuario(Integer id) {
        try {
            String url = funUsuarios + "/" + id;

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