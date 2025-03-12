// HelloController.java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    // Define un endpoint que responde a solicitudes GET en /hello
    @GetMapping("/hello")
    public Map<String, String> hello() {
        // Crea un mapa para la respuesta con mensaje y estado
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hola Mundo");
        response.put("status", "success");
        return response;  // Spring convierte automáticamente a JSON
    }
}