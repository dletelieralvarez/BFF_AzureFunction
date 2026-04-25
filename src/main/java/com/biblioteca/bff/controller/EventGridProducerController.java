package com.biblioteca.bff.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.biblioteca.bff.service.EventGridProducerService;

@RestController
@RequestMapping("/api/eventos")
public class EventGridProducerController {
    private final EventGridProducerService eventGridProducerService;

    public EventGridProducerController(EventGridProducerService eventGridProducerService) {
        this.eventGridProducerService = eventGridProducerService;
    }

    @PostMapping("/prestamo-creado")
    public ResponseEntity<?> publicarPrestamoCreado(
            @RequestParam Integer libroId,
            @RequestParam Integer clienteId) {

        eventGridProducerService.publicarPrestamoCreado(libroId, clienteId);

        return ResponseEntity.ok("Evento PrestamoCreado publicado correctamente");
    }
}
