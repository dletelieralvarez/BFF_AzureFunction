package com.biblioteca.bff.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> listarLibros() {
        return libroBffService.obtenerLibros();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerLibroPorId(@PathVariable Integer id) {
        return libroBffService.obtenerLibroPorId(id);
    }

    @PostMapping
    public ResponseEntity<?> crearLibro(@RequestBody LibroDto request) {
        return libroBffService.crearLibro(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarLibro(@PathVariable Integer id,
                                             @RequestBody LibroDto request) {
        return libroBffService.actualizarLibro(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarLibro(@PathVariable Integer id) {
        return libroBffService.eliminarLibro(id);
    }
}