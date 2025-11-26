package org.example.ejerciciowebdinamica.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Categoria {
    private String id;
    private String nombre;

    public void setNombre(String nombre) {
        this.nombre = nombre.toUpperCase();
    }
}
