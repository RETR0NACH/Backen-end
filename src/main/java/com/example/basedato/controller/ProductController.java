package com.example.basedato.controller;

import com.example.basedato.model.Product;
import com.example.basedato.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // 1. Obtener todos los productos (GET)
    @GetMapping
    public ResponseEntity<List<Product>> getAllProductos() {
        List<Product> productos = productRepository.findAll();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // 2. Obtener un producto por ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductoById(@PathVariable Long id) {
        Optional<Product> producto = productRepository.findById(id);
        return producto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // 3. Crear un producto (POST)
    @PostMapping
    public ResponseEntity<Product> createProducto(@RequestBody Product producto) {
        // Aseguramos que el ID sea null para crear uno nuevo
        producto.setId(null);
        Product nuevoProducto = productRepository.save(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // 4. Actualizar producto (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProducto(@PathVariable Long id, @RequestBody Product detalles) {
        Optional<Product> productoOpt = productRepository.findById(id);

        if (productoOpt.isPresent()) {
            Product producto = productoOpt.get();
            producto.setNombre(detalles.getNombre());
            producto.setPrecio(detalles.getPrecio());
            producto.setCategoria(detalles.getCategoria());
            producto.setImg(detalles.getImg());
            producto.setDescripcion(detalles.getDescripcion());

            return new ResponseEntity<>(productRepository.save(producto), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. Eliminar producto (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProducto(@PathVariable Long id) {
        try {
            productRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
