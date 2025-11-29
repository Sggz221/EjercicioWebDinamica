package org.example.ejerciciowebdinamica.common;

import org.example.ejerciciowebdinamica.errors.ProductoError;
import org.example.ejerciciowebdinamica.exceptions.ProductoException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProductoException.NotFoundException.class)
    public String handleNotFound(ProductoException ex, Model model) {
        ProductoError error = new ProductoError("404", ex.getMessage());
        model.addAttribute("error", error);
        return "productos/errorpage";
    }

    // Captura cualquier otra cosa rara (500)
    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        ProductoError error = new ProductoError("500", "Error interno del servidor: " + ex.getMessage());
        model.addAttribute("error", error);
        return "productos/errorpage";
    }

}
