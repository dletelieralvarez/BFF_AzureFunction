package com.biblioteca.bff.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.biblioteca.bff.dto.PrestamoDto;
import com.biblioteca.bff.service.PrestamoBffService;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoBffController {
    private final PrestamoBffService prestamoBffService;

    public PrestamoBffController(PrestamoBffService prestamoBffService) {
        this.prestamoBffService = prestamoBffService;
    }

    @GetMapping
    public List<PrestamoDto> listarPrestamos() {
        return prestamoBffService.obtenerPrestamos();
    }

    @GetMapping("/{id}")
    public PrestamoDto obtenerPrestamoPorId(@PathVariable Integer id) {
        return prestamoBffService.obtenerPrestamoPorId(id);
    }

    @PostMapping
    public ResponseEntity<?> crearPrestamo(@RequestBody PrestamoDto request) {
        return prestamoBffService.crearPrestamo(request);
    }

    @PutMapping("/{id}")
    public PrestamoDto actualizarPrestamo(@PathVariable Integer id,
                                          @RequestBody PrestamoDto request) {
        return prestamoBffService.actualizarPrestamo(id, request);
    }

    @DeleteMapping("/{id}")
    public String eliminarPrestamo(@PathVariable Integer id) {
        return prestamoBffService.eliminarPrestamo(id);
    }

    @PatchMapping("/{id}/devolucion")
    public ResponseEntity<?> registrarDevolucion(@PathVariable Integer id,
                                                 @RequestBody PrestamoDto request) {
        System.out.println("Entró al endpoint devolucion con id: " + id);
        return prestamoBffService.registrarDevolucion(id, request);
    }
}
