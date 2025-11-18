package com.example.basedato.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "pedidos")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;
    private Integer total;

    @ManyToOne // Muchos pedidos pueden ser de un usuario
    @JoinColumn(name = "usuario_id")
    private User usuario;
}
