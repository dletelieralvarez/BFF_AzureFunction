package com.biblioteca.bff.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.biblioteca.bff.dto.UsuarioDto;
import org.springframework.http.*;

@Service
public class UsuarioBffService {
    private final RestTemplate restTemplate;

    @Value("${azure.functions.usuarios.usuarios.url}")
    private String funUsuarios;

    public UsuarioBffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<UsuarioDto> obtenerUsuarios() {
        ResponseEntity<List<UsuarioDto>> response = restTemplate.exchange(
                funUsuarios,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UsuarioDto>>() {}
        );

        return response.getBody();
    }

    public UsuarioDto obtenerUsuarioPorId(Integer id) {
        String url = funUsuarios + "/" + id;

        ResponseEntity<UsuarioDto> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                UsuarioDto.class
        );

        return response.getBody();
    }

    public UsuarioDto crearUsuario(UsuarioDto request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UsuarioDto> entity = new HttpEntity<>(request, headers);

        ResponseEntity<UsuarioDto> response = restTemplate.exchange(
                funUsuarios,
                HttpMethod.POST,
                entity,
                UsuarioDto.class
        );

        return response.getBody();
    }

    public UsuarioDto actualizarUsuario(Integer id, UsuarioDto request) {
        UsuarioDto body = new UsuarioDto();
        body.setId(id);
        body.setNombre(request.getNombre());
        body.setEmail(request.getEmail());
        body.setRol(request.getRol());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UsuarioDto> entity = new HttpEntity<>(body, headers);

        ResponseEntity<UsuarioDto> response = restTemplate.exchange(
                funUsuarios,
                HttpMethod.PUT,
                entity,
                UsuarioDto.class
        );

        return response.getBody();
    }

    public String eliminarUsuario(Integer id) {
        String url = funUsuarios + "/" + id;

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.DELETE,
                null,
                String.class
        );

        return response.getBody();
    }
}
