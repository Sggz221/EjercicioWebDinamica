package org.example.ejerciciowebdinamica.exceptions;

public class ProductoException extends RuntimeException {
    public ProductoException(String mensaje) {
        super(mensaje);
    }

    public static class InvalidNameException extends ProductoException {
        public InvalidNameException() {
            super("El nombre del producto no puede estar vacío.");
        }
    }

    public static class InvalidDescriptionException extends ProductoException {
        public InvalidDescriptionException() {
            super("La descripción del producto no puede estar vacía.");
        }
    }

    public static class InvalidPriceException extends ProductoException {
        public InvalidPriceException() {
            super("El precio no puede ser inferior a 0.01 €.");
        }
    }

    public static class InvalidCategoryException extends ProductoException {
        public InvalidCategoryException() {
            super("La categoría del producto no existe o es nula.");
        }
    }

    public static class CategoryNotFoundException extends ProductoException {
        public CategoryNotFoundException() {
            super("La categoría no se ha encontrado.");
        }
    }

    public static class NotFoundException extends ProductoException {
        public NotFoundException() {
            super("El producto no existe");
        }
    }
}
