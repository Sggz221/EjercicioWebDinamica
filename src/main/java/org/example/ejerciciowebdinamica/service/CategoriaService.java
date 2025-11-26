package org.example.ejerciciowebdinamica.service;

import lombok.val;
import org.example.ejerciciowebdinamica.models.Categoria;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class CategoriaService {
    private HashMap<String, Categoria> categorias;
    public CategoriaService() {
        this.categorias = new HashMap<>();
        val c1 = Categoria.builder().id(generarId(12)).nombre("TECLADOS").build();
        val c2 =  Categoria.builder().id(generarId(12)).nombre("ALMACENAMIENTO").build();
        val c3 = Categoria.builder().id(generarId(12)).nombre("ACCESORIOS").build();
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
