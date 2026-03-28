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

import com.biblioteca.bff.dto.LibroDto;
import com.biblioteca.bff.service.LibroBffService;

@RestController
@RequestMapping("/api/libros")
public class LibroBffController {
    private final LibroBffService libroBffService;

    public LibroBffController(LibroBffService libroBffService) {
        this.libroBffService = libroBffService;
    }

    @GetMapping
    public List<LibroDto> listarLibros() {
        return libroBffService.obtenerLibros();
    }

    @GetMapping("/{id}")
    public LibroDto obtenerLibroPorId(@PathVariable Integer id) {
        return libroBffService.obtenerLibroPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LibroDto crearLibro(@RequestBody LibroDto request) {
        return libroBffService.crearLibro(request);
    }

    @PutMapping("/{id}")
    public LibroDto actualizarLibro(@PathVariable Integer id,
                                    @RequestBody LibroDto request) {
        return libroBffService.actualizarLibro(id, request);
    }

    @DeleteMapping("/{id}")
    public String eliminarLibro(@PathVariable Integer id) {
        return libroBffService.eliminarLibro(id);
    }
}
