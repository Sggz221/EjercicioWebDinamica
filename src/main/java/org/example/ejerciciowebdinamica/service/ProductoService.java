package org.example.ejerciciowebdinamica.service;

import lombok.val;
import org.example.ejerciciowebdinamica.models.Producto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class ProductoService {
    private HashMap<String, Producto> productos;
    public ProductoService() {
        // Inicializamos el mapa
        this.productos = new HashMap<>();

        val p1 = Producto.builder()
                .id(generarId(12))
                .nombre("Auriculares Bluetooth")
                .descripcion("Cancelación de ruido activa y 20h de batería")
                .precio(59.99)
                .categoria("TECNOLOGÍA")
                .build();

        val p2 = Producto.builder()
                .id(generarId(12))
                .nombre("Teclado Mecánico")
                .descripcion("Retroiluminación RGB y switches azules")
                .precio(85.50)
                .categoria("TECNOLOGÍA")
                .build();

        val p3 = Producto.builder()
                .id(generarId(12))
                .nombre("Monitor 24 Pulgadas")
                .descripcion("Pantalla Full HD ideal para oficina")
                .precio(129.00)
                .categoria("TECNOLOGÍA")
                .build();

        val p4 = Producto.builder()
                .id(generarId(12))
                .nombre("Ratón Gaming")
                .descripcion("Alta precisión 16000 DPI")
                .precio(34.25)
                .categoria("TECNOLOGÍA")
                .build();

        val p5 = Producto.builder()
                .id(generarId(12))
                .nombre("Soporte para Laptop")
                .descripcion("Aluminio ajustable y ergonómico")
                .precio(22.99)
                .categoria("TECNOLOGÍA")
                .build();

        val p6 = Producto.builder()
                .id(generarId(12))
                .nombre("Mochila Antirrobo")
                .descripcion("Impermeable con puerto de carga USB")
                .precio(45.00)
                .categoria("TECNOLOGÍA")
                .build();

        val p7 = Producto.builder()
                .id(generarId(12))
                .nombre("Webcam HD")
                .descripcion("Resolución 1080p con micrófono integrado")
                .precio(29.95)
                .build();

        val p8 = Producto.builder()
                .id(generarId(12))
                .nombre("Disco SSD 1TB")
                .descripcion("Velocidad de lectura ultra rápida")
                .precio(95.00)
                .categoria("TECNOLOGÍA")
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
    }

    public List<Producto> getProductos(Optional<String> nombre, Optional<Double> precioMaximo, String sortBy, String direction) {

        Stream<Producto> stream = getProductoStream(nombre, precioMaximo);

        Comparator<Producto> comparador = switch (sortBy.toLowerCase()) {
            case "precio" -> Comparator.comparing(Producto::getPrecio);
            case "id" -> Comparator.comparing(Producto::getId);
            default -> Comparator.comparing(Producto::getNombre);
        };

        // Decidimos por qué campo ordenar

        if ("desc".equalsIgnoreCase(direction)) {
            comparador = comparador.reversed();
        }

        return stream.sorted(comparador).toList();
    }

    public Optional<Producto> getProducto(String id) {
        return Optional.ofNullable(productos.get(id));
    }

    private Stream<Producto> getProductoStream(Optional<String> nombre, Optional<Double> precioMaximo) {
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
        return stream;
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
