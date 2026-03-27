package com.biblioteca.bff.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
    public List<UsuarioDto> listarUsuarios() {
        return usuarioBffService.obtenerUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioDto obtenerUsuarioPorId(@PathVariable Integer id) {
        return usuarioBffService.obtenerUsuarioPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDto crearUsuario(@RequestBody UsuarioDto request) {
        return usuarioBffService.crearUsuario(request);
    }

    @PutMapping("/{id}")
    public UsuarioDto actualizarUsuario(@PathVariable Integer id,
                                        @RequestBody UsuarioDto request) {
        return usuarioBffService.actualizarUsuario(id, request);
    }

    @DeleteMapping("/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        return usuarioBffService.eliminarUsuario(id);
    }
    
}
