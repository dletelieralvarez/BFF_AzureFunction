package com.biblioteca.bff.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import com.biblioteca.bff.dto.LibroDto;
import org.springframework.beans.factory.annotation.Value;

@Service
public class LibroBffService {
    private final RestTemplate restTemplate;

    @Value("${azure.functions.libros.url}")
    private String funLibros;

    public LibroBffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<LibroDto> obtenerLibros() {
        ResponseEntity<List<LibroDto>> response = restTemplate.exchange(
                funLibros,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<LibroDto>>() {}
        );

        return response.getBody();
    }

    public LibroDto obtenerLibroPorId(Integer id) {
        String url = funLibros + "/" + id;

        ResponseEntity<LibroDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                LibroDto.class
        );

        return response.getBody();
    }

    public LibroDto crearLibro(LibroDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LibroDto> entity = new HttpEntity<>(request, headers);

        ResponseEntity<LibroDto> response = restTemplate.exchange(
                funLibros,
                HttpMethod.POST,
                entity,
                LibroDto.class
        );

        return response.getBody();
    }

    public LibroDto actualizarLibro(Integer id, LibroDto request) {
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

        return response.getBody();
    }

    public String eliminarLibro(Integer id) {
        String url = funLibros + "/" + id;

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class
        );

        return response.getBody();
    }
}
