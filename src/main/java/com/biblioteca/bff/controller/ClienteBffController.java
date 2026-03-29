package com.biblioteca.bff.controller;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> listarClientes() {
        return clienteBffService.obtenerClientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Integer id) {
        return clienteBffService.obtenerClientePorId(id);
    }

    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody ClienteDto request) {
        return clienteBffService.crearCliente(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Integer id,
                                               @RequestBody ClienteDto request) {
        return clienteBffService.actualizarCliente(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Integer id) {
        return clienteBffService.eliminarCliente(id);
    }
}