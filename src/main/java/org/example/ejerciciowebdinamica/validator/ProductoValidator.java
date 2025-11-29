package org.example.ejerciciowebdinamica.validator;

import org.example.ejerciciowebdinamica.exceptions.ProductoException;
import org.example.ejerciciowebdinamica.models.Producto;

public class ProductoValidator {

    public static void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().equals("")) throw new ProductoException.InvalidNameException();
        if (producto.getDescripcion() == null || producto.getDescripcion().equals("")) throw new ProductoException.InvalidDescriptionException();
        if (producto.getPrecio() == null || producto.getPrecio() <= 0.01) throw new ProductoException.InvalidPriceException();
    }
}
