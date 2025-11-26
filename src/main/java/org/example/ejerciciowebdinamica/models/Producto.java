package org.example.ejerciciowebdinamica.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Producto {
    private String id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private Double precio;
}
