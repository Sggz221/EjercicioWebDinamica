package org.example.ejerciciowebdinamica.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoError {
    private String codigo;
    private String mensaje;
}
