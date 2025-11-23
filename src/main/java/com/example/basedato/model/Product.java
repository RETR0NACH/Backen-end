package com.example.basedato.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Double precio; // Tu frontend usa números, así que Double o Integer está bien.

    // En tu frontend usas 'categoria' para filtrar
    @Column(nullable = false)
    private String categoria;

    // En tu frontend usas 'img' para la ruta de la imagen
    @Column(name = "img_url")
    private String img;

    @Column(length = 1000)
    private String descripcion;

    // Constructor vacío (obligatorio para JPA)
    public Product() {}

    // Getters y Setters (Obligatorios)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
