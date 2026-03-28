package com.biblioteca.bff.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import com.biblioteca.bff.dto.PrestamoDto;

@Service
public class PrestamoBffService {
    private final RestTemplate restTemplate;

    @Value("${azure.functions.prestamos.url}")
    private String funPrestamos;

    public PrestamoBffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PrestamoDto> obtenerPrestamos() {
        ResponseEntity<List<PrestamoDto>> response = restTemplate.exchange(
                funPrestamos,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PrestamoDto>>() {}
        );

        return response.getBody();
    }

    public PrestamoDto obtenerPrestamoPorId(Integer id) {
        String url = funPrestamos + "/" + id;

        ResponseEntity<PrestamoDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                PrestamoDto.class
        );

        return response.getBody();
    }

    public PrestamoDto crearPrestamo(PrestamoDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PrestamoDto> entity = new HttpEntity<>(request, headers);

        ResponseEntity<PrestamoDto> response = restTemplate.exchange(
                funPrestamos,
                HttpMethod.POST,
                entity,
                PrestamoDto.class
        );

        return response.getBody();
    }

    public PrestamoDto actualizarPrestamo(Integer id, PrestamoDto request) {
        PrestamoDto body = new PrestamoDto();
        body.setId(id);
        body.setFechaPrestamo(request.getFechaPrestamo());
        body.setFechaEntrega(request.getFechaEntrega());
        body.setUsuariosId(request.getUsuariosId());
        body.setLibrosId(request.getLibrosId());
        body.setClientesId(request.getClientesId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PrestamoDto> entity = new HttpEntity<>(body, headers);

        String url = funPrestamos + "/" + id;

        ResponseEntity<PrestamoDto> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                PrestamoDto.class
        );

        return response.getBody();
    }

    public String eliminarPrestamo(Integer id) {
        String url = funPrestamos + "/" + id;

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class
        );

        return response.getBody();
    }
}
