package org.example.ejerciciowebdinamica.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    private String id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private Double precio;
}
