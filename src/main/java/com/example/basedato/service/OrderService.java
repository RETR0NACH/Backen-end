package com.example.basedato.service;

import com.example.basedato.model.Order;
import com.example.basedato.model.User;
import com.example.basedato.repository.OrderRepository;
import com.example.basedato.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    // Crear un pedido nuevo
    public Order createOrder(Order order, String userEmail) {
        // Buscamos al usuario real en la BD usando el email del token
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        order.setUsuario(user);
        order.setFecha(LocalDateTime.now());

        // Aquí podrías agregar lógica para guardar los detalles (productos) si creaste la entidad DetallePedido

        return orderRepository.save(order);
    }

    // Obtener todos los pedidos (Para el Admin)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
