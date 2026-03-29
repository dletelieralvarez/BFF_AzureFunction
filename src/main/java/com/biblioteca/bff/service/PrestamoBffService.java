package com.biblioteca.bff.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Value;
import com.biblioteca.bff.dto.PrestamoDto;

import tools.jackson.databind.ObjectMapper;

import com.biblioteca.bff.dto.DisponibilidadLibroDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Service
public class PrestamoBffService {
        private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${azure.functions.prestamos.url}")
    private String funPrestamos;

    public PrestamoBffService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public java.util.List<PrestamoDto> obtenerPrestamos() {
        try {
            ResponseEntity<PrestamoDto[]> response = restTemplate.getForEntity(
                    funPrestamos,
                    PrestamoDto[].class
            );

            PrestamoDto[] body = response.getBody();
            return body != null ? java.util.Arrays.asList(body) : java.util.Collections.emptyList();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw construirExcepcion(e);
        }
    }

    public PrestamoDto obtenerPrestamoPorId(Integer id) {
        try {
            String url = funPrestamos + "/" + id;
            ResponseEntity<PrestamoDto> response = restTemplate.getForEntity(url, PrestamoDto.class);
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw construirExcepcion(e);
        }
    }

    public ResponseEntity<?> crearPrestamo(PrestamoDto request) {
        try {            
            DisponibilidadLibroDto disponibilidad = validarDisponibilidadLibro(request.getLibroId());

            if (disponibilidad == null || !disponibilidad.getDisponible()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("{\"mensaje\":\"El libro no está disponible\"}");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PrestamoDto> entity = new HttpEntity<>(request, headers);

            ResponseEntity<PrestamoDto> response = restTemplate.exchange(
                    funPrestamos,
                    HttpMethod.POST,
                    entity,
                    PrestamoDto.class
            );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (RestClientResponseException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());
        }
    }

    public PrestamoDto actualizarPrestamo(Integer id, PrestamoDto request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PrestamoDto> entity = new HttpEntity<>(request, headers);

            String url = funPrestamos + "/" + id;

            ResponseEntity<PrestamoDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    entity,
                    PrestamoDto.class
            );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw construirExcepcion(e);
        }
    }

    public String eliminarPrestamo(Integer id) {
        try {
            String url = funPrestamos + "/" + id;

            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

            return "Préstamo eliminado correctamente";

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw construirExcepcion(e);
        }
    }

    public ResponseEntity<?> registrarDevolucion(Integer id, PrestamoDto request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<PrestamoDto> entity = new HttpEntity<>(request, headers);

            String url = funPrestamos + "/" + id + "/devolucion";
            System.out.println("Llamando a URL interna: " + url);

            ResponseEntity<PrestamoDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.PATCH,
                    entity,
                    PrestamoDto.class
            );

            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (RestClientResponseException e) {
            System.out.println("Error microservicio: " + e.getResponseBodyAsString());

            return ResponseEntity
                    .status(e.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getResponseBodyAsString());
        }
    }

    private ResponseStatusException construirExcepcion(org.springframework.web.client.RestClientResponseException e) {
        String mensaje = extraerMensaje(e.getResponseBodyAsString());
        return new ResponseStatusException(e.getStatusCode(), mensaje, e);
    }

    private String extraerMensaje(String responseBody) {
    try {
            Map<?, ?> body = objectMapper.readValue(responseBody, Map.class);

            if (body.containsKey("mensaje") && body.get("mensaje") != null) {
                return body.get("mensaje").toString();
            }

            if (body.containsKey("message") && body.get("message") != null) {
                return body.get("message").toString();
            }

            if (body.containsKey("error") && body.get("error") != null) {
                return body.get("error").toString();
            }

            return responseBody;

    } catch (Exception ex) {
        return responseBody != null && !responseBody.isBlank()
                ? responseBody
                : "Error al consumir el microservicio de préstamos";
    }
}
    public DisponibilidadLibroDto validarDisponibilidadLibro(Integer libroId) {
        try {
            String url = funPrestamos + "/libros/" + libroId + "/disponibilidad";

            ResponseEntity<DisponibilidadLibroDto> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    DisponibilidadLibroDto.class
            );

            return response.getBody();

        } catch (RestClientResponseException e) {
            throw new ResponseStatusException(
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
        }
    }
}
