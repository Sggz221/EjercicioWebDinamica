package org.example.ejerciciowebdinamica.controller;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.ejerciciowebdinamica.errors.ProductoError;
import org.example.ejerciciowebdinamica.models.Categoria;
import org.example.ejerciciowebdinamica.models.Producto;
import org.example.ejerciciowebdinamica.service.CategoriaService;
import org.example.ejerciciowebdinamica.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping({"/", ""})
@Slf4j
public class PoductoController {
    private ProductoService productoService;
    private CategoriaService categoriaService;
    @Autowired
    public PoductoController( ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    // Categorias para inyectar en productos
    @ModelAttribute("categorias")
    public Categoria[] getCategorias() {
        return categoriaService.findAll();
    }

    @GetMapping
    public String index() {
        return "productos/index";
    }

    @GetMapping({"/productos", "/productos/"})
    public String getProductos(Model model,
                               @RequestParam(required = false) Optional<String> nombre,
                               @RequestParam(required = false) Optional<Double> precioMaximo,
                               @RequestParam(required = false) Optional<String> categoria,
                               @RequestParam(defaultValue = "id") String sortBy,
                               @RequestParam(defaultValue = "asc") String direction) {
        model.addAttribute("productos", productoService.getProductos(nombre, precioMaximo, categoria, sortBy, direction));
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

    @GetMapping("/productos/crear")
    public String crearProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/form";
    }

    @PostMapping("/productos/crear/submit")
    public String crearProducto(@ModelAttribute("producto") Producto producto) {
        try {
            log.info("Guardando producto");
            final String id = productoService.saveProducto(producto);
            return "redirect:/productos/" + id;
        } catch (Exception e) {
            log.info("Error al crear producto: " + e.getMessage());
            return "redirect:/productos/";
        }
    }
}
