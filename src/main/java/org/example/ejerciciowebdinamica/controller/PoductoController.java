package org.example.ejerciciowebdinamica.controller;

import lombok.val;
import org.example.ejerciciowebdinamica.exceptions.ProductoError;
import org.example.ejerciciowebdinamica.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
@RequestMapping({"/", ""})
public class PoductoController {
    private ProductoService productoService;
    @Autowired
    public PoductoController( ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String index() {
        return "productos/index";
    }

    @GetMapping({"/productos", "/productos/"})
    public String getProductos(Model model,
                               @RequestParam(required = false) Optional<String> nombre,
                               @RequestParam(required = false) Optional<Double> precioMaximo,
                               @RequestParam(defaultValue = "id") String sortBy,
                               @RequestParam(defaultValue = "asc") String direction) {
        String sort = direction.equals("asc") ? "asc" : "desc";
        model.addAttribute("productos", productoService.getProductos(nombre, precioMaximo, sortBy, direction));
        return "productos/lista";
    }

    @GetMapping("productos/{id}")
    public String getProducto(@PathVariable String id, Model model) {
        val producto = productoService.getProducto(id);
        if (producto.isEmpty()) {
            val error = new ProductoError("No existe el producto con el id: " + id);
            model.addAttribute("mensaje", error.getMensaje());
            return "productos/404";
        }
        model.addAttribute("producto", producto.get());
        return "productos/detalle";
    }
}
