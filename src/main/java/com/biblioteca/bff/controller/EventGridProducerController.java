package com.biblioteca.bff.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.biblioteca.bff.dto.LibroDto;
import com.biblioteca.bff.dto.PrestamoDto;
import com.biblioteca.bff.service.EventGridProducerService;

@RestController
@RequestMapping("/api/eventos")
public class EventGridProducerController {
    private final EventGridProducerService eventGridProducerService;

    public EventGridProducerController(EventGridProducerService eventGridProducerService) {
        this.eventGridProducerService = eventGridProducerService;
    }

    @PostMapping("/prestamo-creado")
    public ResponseEntity<?> crearPrestamo(@RequestBody PrestamoDto request) {
        ResponseEntity<?> response = eventGridProducerService.crearPrestamo(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            eventGridProducerService.publicarPrestamoCreado(
                    request.getLibroId(),
                    request.getClienteId()
            );
        }

        return response;
    }

    @PostMapping("/libro-creado")
    public ResponseEntity<?> crearLibro(@RequestBody LibroDto request) {

        ResponseEntity<?> response = eventGridProducerService.crearLibro(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            eventGridProducerService.publicarLibroCreado(
                    response.getBody() != null ? ((LibroDto) response.getBody()).getId() : null,
                    request.getTitulo(),
                    request.getAutor()
            );
        }

        return response;
    }
}
