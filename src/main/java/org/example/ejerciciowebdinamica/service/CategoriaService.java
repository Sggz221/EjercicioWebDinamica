package org.example.ejerciciowebdinamica.service;

import lombok.val;
import org.example.ejerciciowebdinamica.models.Categoria;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

@Service
public class CategoriaService {
    private HashMap<String, Categoria> categorias;
    public CategoriaService() {
        this.categorias = new HashMap<>();
        val c1 = Categoria.builder().id(generarId(12)).nombre("TECLADOS").build();
        val c2 =  Categoria.builder().id(generarId(12)).nombre("ALMACENAMIENTO").build();
        val c3 = Categoria.builder().id(generarId(12)).nombre("ACCESORIOS").build();
        val c4 = Categoria.builder().id(generarId(12)).nombre("MONITORES").build();
        val c5 = Categoria.builder().id(generarId(12)).nombre("RATONES").build();

        categorias.put(c1.getId(), c1);
        categorias.put(c2.getId(), c2);
        categorias.put(c3.getId(), c3);
        categorias.put(c4.getId(), c4);
        categorias.put(c5.getId(), c5);
    }

    public Optional<Categoria> getByName(String nombre) {
        for(Categoria categoria : categorias.values()){
            if(categoria.getNombre().equals(nombre)) return Optional.of(categoria);
        }
        return Optional.empty();
    }

    public Optional<Categoria> getById(String id) {
        return Optional.ofNullable(categorias.get(id));
    }

    public Categoria[] findAll() {
        return categorias.values().toArray(new Categoria[categorias.size()]);
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
