package com.biblioteca.bff.dto;

public class DisponibilidadLibroDto {
    private Integer libroId;
    private Boolean disponible;

    public Integer getLibroId() {
        return libroId;
    }

    public void setLibroId(Integer libroId) {
        this.libroId = libroId;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }
}
