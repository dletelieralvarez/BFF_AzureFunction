package com.biblioteca.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.biblioteca.bff.dto.UsuarioDto;
import com.biblioteca.bff.service.UsuarioBffService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioBffController {

    private final UsuarioBffService usuarioBffService;

    public UsuarioBffController(UsuarioBffService usuarioBffService) {
        this.usuarioBffService = usuarioBffService;
    }

    @GetMapping
    public ResponseEntity<?> listarUsuarios() {
        return usuarioBffService.obtenerUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable Integer id) {
        return usuarioBffService.obtenerUsuarioPorId(id);
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDto request) {
        return usuarioBffService.crearUsuario(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer id,
                                               @RequestBody UsuarioDto request) {
        return usuarioBffService.actualizarUsuario(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        return usuarioBffService.eliminarUsuario(id);
    }
}