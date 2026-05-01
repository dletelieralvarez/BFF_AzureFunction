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

import com.biblioteca.bff.dto.ClienteDto;
import com.biblioteca.bff.dto.LibroDto;
import com.biblioteca.bff.dto.PrestamoDto;
import com.biblioteca.bff.dto.UsuarioDto;

@Service
public class EventGridProducerService {
    private final RestTemplate restTemplate;

    @Value("${azure.functions.prestamos.url}")
    private String funPrestamos;

    @Value("${azure.functions.libros.url}")
    private String funLibros;

    @Value("${azure.functions.usuarios.usuarios.url}")
    private String funUsuarios;

    @Value("${azure.functions.clientes.url}")
    private String funClientes;

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
    public ResponseEntity<?> consultaPrestamo(PrestamoDto request) {
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
    public void publicarConsultaPrestamo(Integer libroId, Integer usuarioId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("aeg-sas-key", eventGridKey);

        Map<String, Object> evento = Map.of(
                "id", UUID.randomUUID().toString(),
                "eventType", "ConsultaPrestamo",
                "subject", "biblioteca/prestamos",
                "eventTime", OffsetDateTime.now().toString(),
                "data", Map.of(
                        "mensaje", "Consulta de prestamo desde BFF",
                        "libroId", libroId,
                        "usuarioId", usuarioId
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

        public ResponseEntity<?> crearUsuario(UsuarioDto request) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<UsuarioDto> entity = new HttpEntity<>(request, headers);

                ResponseEntity<UsuarioDto> response = restTemplate.exchange(
                        funUsuarios,
                        HttpMethod.POST,
                        entity,
                        UsuarioDto.class
                );

                publicarEvento(
                        "UsuarioCreado",
                        "biblioteca/usuarios",
                        Map.of(
                                "mensaje", "Usuario creado desde BFF",
                                "nombre", request.getNombre(),
                                "email", request.getEmail()
                        )
                );

                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        public ResponseEntity<?> crearCliente(ClienteDto request) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<ClienteDto> entity = new HttpEntity<>(request, headers);

                ResponseEntity<ClienteDto> response = restTemplate.exchange(
                        funClientes,
                        HttpMethod.POST,
                        entity,
                        ClienteDto.class
                );

                publicarEvento(
                        "ClienteCreado",
                        "biblioteca/clientes",
                        Map.of(
                                "mensaje", "Cliente creado desde BFF",
                                "rut", request.getRut(),
                                "nombre", request.getNombre()
                        )
                );

                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        public ResponseEntity<?> actualizarCliente(Integer id, ClienteDto request) {
                String url = funClientes + "/" + id;

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<ClienteDto> entity = new HttpEntity<>(request, headers);

                ResponseEntity<ClienteDto> response = restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        entity,
                        ClienteDto.class
                );

                publicarEvento(
                        "ClienteActualizado",
                        "biblioteca/clientes",
                        Map.of(
                                "mensaje", "Cliente actualizado desde BFF",
                                "clienteId", id,
                                "rut", request.getRut(),
                                "nombre", request.getNombre()
                        )
                );

                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        public ResponseEntity<?> eliminarCliente(Integer id) {
                String url = funClientes + "/" + id;

                ResponseEntity<String> response = restTemplate.exchange(
                        url,
                        HttpMethod.DELETE,
                        null,
                        String.class
                );

                publicarEvento(
                        "ClienteEliminado",
                        "biblioteca/clientes",
                        Map.of(
                                "mensaje", "Cliente eliminado desde BFF",
                                "clienteId", id
                        )
                );

                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }

        public void publicarEvento(String eventType, String subject, Map<String, Object> data) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("aeg-sas-key", eventGridKey);

                Map<String, Object> evento = Map.of(
                        "id", UUID.randomUUID().toString(),
                        "eventType", eventType,
                        "subject", subject,
                        "eventTime", OffsetDateTime.now().toString(),
                        "data", data,
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