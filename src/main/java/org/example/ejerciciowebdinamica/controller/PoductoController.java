package org.example.ejerciciowebdinamica.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.ejerciciowebdinamica.errors.ProductoError;
import org.example.ejerciciowebdinamica.exceptions.ProductoException;
import org.example.ejerciciowebdinamica.models.Categoria;
import org.example.ejerciciowebdinamica.models.Producto;
import org.example.ejerciciowebdinamica.service.CategoriaService;
import org.example.ejerciciowebdinamica.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
                               @RequestParam(defaultValue = "asc") String direction,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               HttpSession httpSession) {
        // Visitas
        Integer visitas = (Integer) httpSession.getAttribute("visitas");
        if (visitas == null) visitas = 0;
        visitas++;
        httpSession.setAttribute("visitas", visitas);
        // Ultimo producto, visitas se controla en GlobalAdvice
        String ultimoProducto = (String) httpSession.getAttribute("ultimoProducto");

        if (ultimoProducto == null) ultimoProducto = "Ninguno";

        model.addAttribute("ultimoProducto", ultimoProducto);
        model.addAttribute("visitas", visitas);
        model.addAttribute("productos", productoService.getProductos(nombre, precioMaximo, categoria, sortBy, direction, page, size));
        return "productos/lista";
    }

    @GetMapping("productos/{id}")
    public String getProducto(@PathVariable String id, Model model, HttpSession httpSession) {
        val producto = productoService.getProducto(id);
        if (producto.isEmpty()) {
            val error = new ProductoError("404", "No existe el producto con el id: " + id);
            model.addAttribute("error", error);
            return "productos/errorpage";
        }
        model.addAttribute("producto", producto.get());
        httpSession.setAttribute("ultimoProducto", producto.get().getNombre());
        return "productos/detalle";
    }

    @GetMapping("/productos/crear")
    public String crearProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/form";
    }

    @GetMapping("/productos/editar/{id}")
    public String editarProducto(@PathVariable String id, Model model) {
        val producto = productoService.getProducto(id);
        if (producto.isEmpty()) throw new ProductoException.NotFoundException();
        model.addAttribute("producto", producto.get());
        return "productos/form";
    }

    @PostMapping("/productos/crear/submit")
    public String crearProductoSubmit(@ModelAttribute("producto") Producto producto, BindingResult bindingResult, Model model) {
        try {
            log.info("Guardando producto...");

            // Esto llama a tu validador interno. Si falla, lanza excepción.
            final String id = productoService.saveProducto(producto);

            log.info("Producto guardado correctamente");
            return "redirect:/productos/" + id;

        } catch (ProductoException e) {
            log.info("Error de validación capturado: " + e.getMessage());

            // AQUI ESTA LA MAGIA: Mapeamos tu excepción al campo correspondiente de Spring
            switch (e) {
                case ProductoException.InvalidNameException invalidNameException ->
                    // Error asociado al campo 'nombre'
                        bindingResult.rejectValue("nombre", "error.nombre", e.getMessage());
                case ProductoException.InvalidPriceException invalidPriceException ->
                    // Error asociado al campo 'precio'
                        bindingResult.rejectValue("precio", "error.precio", e.getMessage());
                case ProductoException.InvalidDescriptionException invalidDescriptionException ->
                    // Error asociado al campo 'descripcion'
                        bindingResult.rejectValue("descripcion", "error.descripcion", e.getMessage());
                case ProductoException.InvalidCategoryException invalidCategoryException ->
                    // Error asociado al campo 'categoria'
                        bindingResult.rejectValue("categoria", "error.categoria", e.getMessage());
                default ->
                    // Si es un error genérico (ej. base de datos, o algo que no es de un campo específico)
                    // Se usa el reject global (3 argumentos para asegurar que el mensaje se guarde)
                        bindingResult.reject("error.global", null, e.getMessage());
            }

            // Volvemos al formulario. Spring ahora ya sabe qué campo pintar de rojo.
            return "productos/form";
        }
    }

    @PostMapping("/productos/editar/submit")
    public String editarProductoSubmit(@ModelAttribute("producto") Producto producto, BindingResult bindingResult) {
        try {
            log.info("Actualizando producto con ID: " + producto.getId());

            productoService.updateProducto(producto.getId(), producto);

            log.info("Producto actualizado correctamente");
            return "redirect:/productos/" + producto.getId();

        } catch (ProductoException e) {
            log.info("Error de validación al editar: " + e.getMessage());

            switch (e) {
                case ProductoException.InvalidNameException invalidNameException ->
                        bindingResult.rejectValue("nombre", "error.nombre", e.getMessage());
                case ProductoException.InvalidPriceException invalidPriceException ->
                        bindingResult.rejectValue("precio", "error.precio", e.getMessage());
                case ProductoException.InvalidDescriptionException invalidDescriptionException ->
                        bindingResult.rejectValue("descripcion", "error.descripcion", e.getMessage());
                case ProductoException.InvalidCategoryException invalidCategoryException ->
                        bindingResult.rejectValue("categoria", "error.categoria", e.getMessage());
                default ->
                        bindingResult.reject("error.global", null, e.getMessage());
            }

            return "productos/form";
        }
    }

    @PostMapping("/productos/borrar/{id}")
    public String borrarProducto(@PathVariable String id) {
        productoService.deleteProducto(id);
        return "redirect:/productos";
    }
}
