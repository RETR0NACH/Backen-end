package com.example.basedato.controller;

import com.example.basedato.model.Product;
import com.example.basedato.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products") // Define la ruta base: http://localhost:8080/api/v1/products
@CrossOrigin(origins = "http://localhost:5173") // ¡IMPORTANTE! Permite que React (Vite) se conecte
public class ProductController {

    @Autowired
    private ProductService productService;

    // 1. Obtener todos los productos (GET /api/v1/products)
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 2. Obtener un producto por ID (GET /api/v1/products/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        // Si existe devuelve 200 OK, si no, 404 Not Found
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 3. Crear un producto (POST /api/v1/products) - (Idealmente solo Admin)
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    // 4. Eliminar un producto (DELETE /api/v1/products/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
