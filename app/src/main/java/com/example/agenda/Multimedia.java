package com.example.agenda;

public class Multimedia {
    private int Id;
    private String ruta;
    private String tipo;
    private int idNota;

    public Multimedia(int id, String ruta, String tipo, int idNota) {
        Id = id;
        this.ruta = ruta;
        this.tipo = tipo;
        this.idNota = idNota;
    }
    public Multimedia(String ruta, String tipo, int idNota) {
        this.ruta = ruta;
        this.tipo = tipo;
        this.idNota = idNota;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdNota() {
        return idNota;
    }

    public void setIdNota(int idNota) {
        this.idNota = idNota;
    }
}
