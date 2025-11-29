package org.example.ejerciciowebdinamica.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.example.ejerciciowebdinamica.exceptions.ProductoException;
import org.example.ejerciciowebdinamica.models.Producto;
import org.example.ejerciciowebdinamica.validator.ProductoValidator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
public class ProductoService {
    private final CategoriaService categoriaService;
    private HashMap<String, Producto> productos;
    public ProductoService(CategoriaService categoriaService) {
        // Inicializamos el mapa
        this.productos = new HashMap<>();

        val p1 = Producto.builder()
                .id(generarId(12))
                .nombre("Auriculares Bluetooth")
                .descripcion("Cancelación de ruido activa y 20h de batería")
                .precio(59.99)
                .categoria("ACCESORIOS")
                .build();

        val p2 = Producto.builder()
                .id(generarId(12))
                .nombre("Teclado Mecánico")
                .descripcion("Retroiluminación RGB y switches azules")
                .precio(85.50)
                .categoria("TECLADOS")
                .build();

        val p3 = Producto.builder()
                .id(generarId(12))
                .nombre("Monitor 24 Pulgadas")
                .descripcion("Pantalla Full HD ideal para oficina")
                .precio(129.00)
                .categoria("MONITORES")
                .build();

        val p4 = Producto.builder()
                .id(generarId(12))
                .nombre("Ratón Gaming")
                .descripcion("Alta precisión 16000 DPI")
                .precio(34.25)
                .categoria("RATONES")
                .build();

        val p5 = Producto.builder()
                .id(generarId(12))
                .nombre("Soporte para Laptop")
                .descripcion("Aluminio ajustable y ergonómico")
                .precio(22.99)
                .categoria("ACCESORIOS")
                .build();

        val p6 = Producto.builder()
                .id(generarId(12))
                .nombre("Mochila Antirrobo")
                .descripcion("Impermeable con puerto de carga USB")
                .precio(45.00)
                .categoria("ACCESORIOS")
                .build();

        val p7 = Producto.builder()
                .id(generarId(12))
                .nombre("Webcam HD")
                .descripcion("Resolución 1080p con micrófono integrado")
                .precio(29.95)
                .categoria("ACCESORIOS")
                .build();

        val p8 = Producto.builder()
                .id(generarId(12))
                .nombre("Disco SSD 1TB")
                .descripcion("Velocidad de lectura ultra rápida")
                .precio(95.00)
                .categoria("ALMACENAMIENTO")
                .build();

        // Insertamos todo en el mapa
        this.productos.put(p1.getId(), p1);
        this.productos.put(p2.getId(), p2);
        this.productos.put(p3.getId(), p3);
        this.productos.put(p4.getId(), p4);
        this.productos.put(p5.getId(), p5);
        this.productos.put(p6.getId(), p6);
        this.productos.put(p7.getId(), p7);
        this.productos.put(p8.getId(), p8);
        this.categoriaService = categoriaService;
    }

    public List<Producto> getProductos(Optional<String> nombre,
                                  Optional<Double> precioMaximo,
                                  Optional<String> categoria,
                                  String sortBy,
                                  String direction,
                                  int page,
                                  int size) {
        log.info("Obteniendo productos...");

        // 1. Obtenemos el stream filtrado
        Stream<Producto> stream = getProductoStream(nombre, precioMaximo, categoria);

        // 2. Configuramos el comparador
        Comparator<Producto> comparador = switch (sortBy.toLowerCase()) {
            case "precio" -> Comparator.comparing(Producto::getPrecio);
            case "id" -> Comparator.comparing(Producto::getId);
            default -> Comparator.comparing(Producto::getNombre);
        };

        if ("desc".equalsIgnoreCase(direction)) {
            comparador = comparador.reversed();
        }

        return stream.sorted(comparador)
                .skip((long) page * size) // Se empieza por la pagina 0 ojo
                .limit(size)
                .toList();
    }

    private Stream<Producto> getProductoStream(Optional<String> nombre, Optional<Double> precioMaximo, Optional<String> categoria) {
        Stream<Producto> stream = productos.values().stream();

        // Filtro por Nombre (Ignorando mayúsculas/minúsculas)
        if (nombre.isPresent()) {
            String textoBusqueda = nombre.get().toLowerCase();
            stream = stream.filter(p -> p.getNombre().toLowerCase().contains(textoBusqueda));
        }

        // Filtro por Precio Máximo
        if (precioMaximo.isPresent()) {
            stream = stream.filter(p -> p.getPrecio() <= precioMaximo.get());
        }
        // Filtro por categoría
        if (categoria.isPresent()) {
            stream = stream.filter(p -> p.getCategoria().toUpperCase().contains(categoria.get().toUpperCase()));
        }
        return stream;
    }

    public Optional<Producto> getProducto(String id) {
        return Optional.ofNullable(productos.get(id));
    }

    public String saveProducto(Producto producto) {
        log.info("Guardando producto...");
        ProductoValidator.validarProducto(producto); // Lanzará una excepción si no es válido
        val id = generarId(12);
        producto.setId(id);
        productos.put(id, producto);
        log.info("Producto guardado");
        return id;
    }

    public String updateProducto(String id, Producto producto) {
        log.info("Actualizando producto...");
        if (categoriaService.getByName(producto.getCategoria()).isEmpty()) throw new ProductoException.CategoryNotFoundException();
        ProductoValidator.validarProducto(producto); // Lanza una excepcion
        if (!productos.containsKey(id)) throw new ProductoException.NotFoundException();
        productos.put(id, producto);
        log.info("Producto actualizado");
        return id;
    }

    public String deleteProducto(String id) {
        log.info("Eliminando producto...");
        if (!productos.containsKey(id)) throw new ProductoException.NotFoundException();
        productos.remove(id);
        log.info("Producto eliminado");
        return id;
    }

    private String generarId(Integer longitud) {
        String charArray = "QWRTYPSDFGHJKLZXCVBNMqwrtypsdfghjklzxcvbnm1234567890-";
        StringBuilder id = new StringBuilder();
        for  (int i = 0; i <= longitud ; i++) {
            id.append(charArray.charAt(new Random().nextInt(charArray.length())));
        }
        return id.toString();
    }

}
