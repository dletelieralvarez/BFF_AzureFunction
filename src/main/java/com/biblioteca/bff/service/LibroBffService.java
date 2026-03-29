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

import com.biblioteca.bff.dto.LibroDto;

@Service
public class LibroBffService {

    private final RestTemplate restTemplate;

    @Value("${azure.functions.libros.url}")
    private String funLibros;

    public LibroBffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> obtenerLibros() {
        try {
            ResponseEntity<List<LibroDto>> response = restTemplate.exchange(
                    funLibros,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<LibroDto>>() {}
            );

            List<LibroDto> body = response.getBody();
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

    public ResponseEntity<?> obtenerLibroPorId(Integer id) {
        try {
            String url = funLibros + "/" + id;

            ResponseEntity<LibroDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
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

    public ResponseEntity<?> actualizarLibro(Integer id, LibroDto request) {
        try {
            LibroDto body = new LibroDto();
            body.setId(id);
            body.setTitulo(request.getTitulo());
            body.setAutor(request.getAutor());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<LibroDto> entity = new HttpEntity<>(body, headers);

            String url = funLibros + "/" + id;

            ResponseEntity<LibroDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
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

    public ResponseEntity<?> eliminarLibro(Integer id) {
        try {
            String url = funLibros + "/" + id;

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