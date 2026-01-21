package com.encuestas.dominio;

public class Pregunta {
    private int id;
    private String texto;
    private int valorMin;
    private int valorMax;
    private int encuestaId;
    private boolean esTextoLibre;

    public Pregunta() {
    }

    public Pregunta(int id, String texto, int valorMin, int valorMax, int encuestaId) {
        this.id = id;
        this.texto = texto;
        this.valorMin = valorMin;
        this.valorMax = valorMax;
        this.encuestaId = encuestaId;
        this.esTextoLibre = false;
    }

    public Pregunta(int id, String texto, int valorMin, int valorMax, int encuestaId, boolean esTextoLibre) {
        this.id = id;
        this.texto = texto;
        this.valorMin = valorMin;
        this.valorMax = valorMax;
        this.encuestaId = encuestaId;
        this.esTextoLibre = esTextoLibre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getValorMin() {
        return valorMin;
    }

    public void setValorMin(int valorMin) {
        this.valorMin = valorMin;
    }

    public int getValorMax() {
        return valorMax;
    }

    public void setValorMax(int valorMax) {
        this.valorMax = valorMax;
    }

    public int getEncuestaId() {
        return encuestaId;
    }

    public void setEncuestaId(int encuestaId) {
        this.encuestaId = encuestaId;
    }

    public boolean isEsTextoLibre() {
        return esTextoLibre;
    }

    public void setEsTextoLibre(boolean esTextoLibre) {
        this.esTextoLibre = esTextoLibre;
    }

    @Override
    public String toString() {
        return texto;
    }
}
