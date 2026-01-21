package com.encuestas.dominio;

public class OpcionRespuesta {
    private int id;
    private int preguntaId;
    private int valor;
    private String label;

    public OpcionRespuesta() {
    }

    public OpcionRespuesta(int id, int preguntaId, int valor, String label) {
        this.id = id;
        this.preguntaId = preguntaId;
        this.valor = valor;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(int preguntaId) {
        this.preguntaId = preguntaId;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return valor + " - " + label;
    }
}
