package com.biblioteca.bff.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.*;

import com.biblioteca.bff.dto.ClienteDto;
import com.biblioteca.bff.service.ClienteBffService;

@RestController
@RequestMapping("/api/clientes")
public class ClienteBffController {
    private final ClienteBffService clienteBffService;

    public ClienteBffController(ClienteBffService clienteBffService) {
        this.clienteBffService = clienteBffService;
    }

    @GetMapping
    public List<ClienteDto> listarClientes() {
        return clienteBffService.obtenerClientes();
    }

    @GetMapping("/{id}")
    public ClienteDto obtenerClientePorId(@PathVariable Integer id) {
        return clienteBffService.obtenerClientePorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDto crearCliente(@RequestBody ClienteDto request) {
        return clienteBffService.crearCliente(request);
    }

    @PutMapping("/{id}")
    public ClienteDto actualizarCliente(@PathVariable Integer id,
                                        @RequestBody ClienteDto request) {
        return clienteBffService.actualizarCliente(id, request);
    }

    @DeleteMapping("/{id}")
    public String eliminarCliente(@PathVariable Integer id) {
        return clienteBffService.eliminarCliente(id);
    }
}
