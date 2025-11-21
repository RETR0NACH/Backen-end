package com.example.basedato.controller;

import com.example.basedato.model.Order;
import com.example.basedato.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // POST: Crear pedido (Solo usuarios logueados)
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        // Obtenemos el email del usuario desde el Token JWT automáticamente
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Order newOrder = orderService.createOrder(order, email);
        return ResponseEntity.ok(newOrder);
    }

    // GET: Ver todos los pedidos (Solo Admin)
    // Asegúrate de proteger esta ruta en SecurityConfig
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }
}
