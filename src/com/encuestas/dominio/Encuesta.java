package com.encuestas.dominio;

import java.time.LocalDate;

public class Encuesta {
    private int id;
    private String titulo;
    private LocalDate fechaCreacion;
    private boolean activa;

    public Encuesta() {
    }

    public Encuesta(int id, String titulo, LocalDate fechaCreacion, boolean activa) {
        this.id = id;
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.activa = activa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isActiva() {
        return activa;
    }


    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return titulo + (activa ? " (Activa)" : " (Inactiva)");
    }
}
