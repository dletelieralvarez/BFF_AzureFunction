package com.biblioteca.bff.controller;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.biblioteca.bff.dto.LibroDto;
import com.biblioteca.bff.service.LibroBffService;

@Controller
public class LibroBffGraphqlController {
    private final LibroBffService libroBffService;

    public LibroBffGraphqlController(LibroBffService libroBffService) {
        this.libroBffService = libroBffService;
    }

    @QueryMapping
    public LibroDto obtenerLibroPorIdGraphQL(@Argument Integer id) {
        return libroBffService.obtenerLibroPorId_GraphQL(id);
    }
}
