package com.biblioteca.bff.controller;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.biblioteca.bff.dto.ClienteDto;
import com.biblioteca.bff.dto.LibroDto;
import com.biblioteca.bff.dto.PrestamoDto;
import com.biblioteca.bff.dto.UsuarioDto;
import com.biblioteca.bff.service.EventGridProducerService;

@RestController
@RequestMapping("/api/eventos")
public class EventGridProducerController {
    private final EventGridProducerService eventGridProducerService;

    public EventGridProducerController(EventGridProducerService eventGridProducerService) {
        this.eventGridProducerService = eventGridProducerService;
    }

    @PostMapping("/consulta-prestamo")
    public ResponseEntity<?> consultaPrestamo(@RequestBody PrestamoDto request) {
        ResponseEntity<?> response = eventGridProducerService.consultaPrestamo(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            eventGridProducerService.publicarConsultaPrestamo(
                    request.getLibroId(),
                    request.getUsuarioId()
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

    @PostMapping("/crear-usuario")
    public ResponseEntity<?> crearUsuario(@RequestBody UsuarioDto request) {
        ResponseEntity<?> response = eventGridProducerService.crearUsuario(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            eventGridProducerService.publicarEvento(
                    "UsuarioCreado",
                    "biblioteca/usuarios",
                    Map.of(
                            "mensaje", "Usuario creado desde BFF",
                            "id", response.getBody() != null ? ((UsuarioDto) response.getBody()).getId() : null,
                            "nombre", request.getNombre(),
                            "email", request.getEmail(),
                            "rol", request.getRol()
                    )
            );
        }

        return response;
    }
    @PostMapping("/crear-cliente")
    public ResponseEntity<?> crearCliente(@RequestBody ClienteDto request) {
        ResponseEntity<?> response = eventGridProducerService.crearCliente(request);

        if (response.getStatusCode().is2xxSuccessful()) {
            eventGridProducerService.publicarEvento(
                    "ClienteCreado",
                    "biblioteca/clientes",
                    Map.of(
                            "mensaje", "Cliente creado desde BFF",
                            "id", response.getBody() != null ? ((ClienteDto) response.getBody()).getId() : null,
                            "nombre", request.getNombre(),
                            "rut", request.getRut(),
                            "dv", request.getDv()
                    )
            );
        }

        return response;
    }

    @PutMapping("/actualizar-cliente/{id}")
    public ResponseEntity<?> actualizarCliente(
            @PathVariable Integer id,
            @RequestBody ClienteDto request) {

        ResponseEntity<?> response = eventGridProducerService.actualizarCliente(id, request);

        if (response.getStatusCode().is2xxSuccessful()) {
            eventGridProducerService.publicarEvento(
                    "ClienteActualizado",
                    "biblioteca/clientes",
                    Map.of(
                            "mensaje", "Cliente actualizado desde BFF",
                            "clienteId", id,
                            "nombre", request.getNombre(),
                            "rut", request.getRut(),
                            "dv", request.getDv()
                    )
            );
        }

        return response;
    }

    @DeleteMapping("/eliminar-cliente/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Integer id) {
        ResponseEntity<?> response = eventGridProducerService.eliminarCliente(id);

        if (response.getStatusCode().is2xxSuccessful()) {
            eventGridProducerService.publicarEvento(
                    "ClienteEliminado",
                    "biblioteca/clientes",
                    Map.of(
                            "mensaje", "Cliente eliminado desde BFF",
                            "clienteId", id
                    )
            );
        }

        return response;
    }
}
