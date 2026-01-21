package com.encuestas.dominio;

import java.time.LocalDateTime;

public class Respuesta {
    private int id;
    private int encuestaId;
    private int preguntaId;
    private int valorSeleccionado;
    private String respuestaTexto;
    private LocalDateTime fechaRespuesta;

    public Respuesta() {
    }

    public Respuesta(int id, int encuestaId, int preguntaId, int valorSeleccionado, LocalDateTime fechaRespuesta) {
        this.id = id;
        this.encuestaId = encuestaId;
        this.preguntaId = preguntaId;
        this.valorSeleccionado = valorSeleccionado;
        this.fechaRespuesta = fechaRespuesta;
    }

    public Respuesta(int id, int encuestaId, int preguntaId, int valorSeleccionado, String respuestaTexto, LocalDateTime fechaRespuesta) {
        this.id = id;
        this.encuestaId = encuestaId;
        this.preguntaId = preguntaId;
        this.valorSeleccionado = valorSeleccionado;
        this.respuestaTexto = respuestaTexto;
        this.fechaRespuesta = fechaRespuesta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEncuestaId() {
        return encuestaId;
    }

    public void setEncuestaId(int encuestaId) {
        this.encuestaId = encuestaId;
    }

    public int getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(int preguntaId) {
        this.preguntaId = preguntaId;
    }

    public int getValorSeleccionado() {
        return valorSeleccionado;
    }

    public void setValorSeleccionado(int valorSeleccionado) {
        this.valorSeleccionado = valorSeleccionado;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }

    public void setRespuestaTexto(String respuestaTexto) {
        this.respuestaTexto = respuestaTexto;
    }

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }
}
