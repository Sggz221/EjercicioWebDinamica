package org.example.ejerciciowebdinamica.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoError {
    private String mensaje;
}
