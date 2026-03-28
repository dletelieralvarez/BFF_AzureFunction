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
import com.biblioteca.bff.dto.ClienteDto;

@Service
public class ClienteBffService {
    private final RestTemplate restTemplate;

    @Value("${azure.functions.clientes.url}")
    private String funClientes;

    public ClienteBffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ClienteDto> obtenerClientes() {
        ResponseEntity<List<ClienteDto>> response = restTemplate.exchange(
                funClientes,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ClienteDto>>() {}
        );

        return response.getBody();
    }

    public ClienteDto obtenerClientePorId(Integer id) {
        String url = funClientes + "/" + id;

        ResponseEntity<ClienteDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                ClienteDto.class
        );

        return response.getBody();
    }

    public ClienteDto crearCliente(ClienteDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ClienteDto> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ClienteDto> response = restTemplate.exchange(
                funClientes,
                HttpMethod.POST,
                entity,
                ClienteDto.class
        );

        return response.getBody();
    }

    public ClienteDto actualizarCliente(Integer id, ClienteDto request) {
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

        return response.getBody();
    }

    public String eliminarCliente(Integer id) {
        String url = funClientes + "/" + id;

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class
        );

        return response.getBody();
    }
}
