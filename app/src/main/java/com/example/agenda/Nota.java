package com.example.agenda;

public class Nota {
    private int ID;
    private String Titulo;
    private String Texto;
    private String Tipo;
    private String Fecha;
    private String Hora;
    private String estado;

    public Nota(int ID, String estado) {
        this.ID = ID;
        this.estado = estado;
    }
    public Nota(int ID, String titulo, String texto, String tipo) {
        this.ID = ID;
        this.Titulo = titulo;
        this.Texto = texto;
        this.Tipo = tipo;
    }
    public Nota(int ID, String titulo, String texto) {
        this.ID = ID;
        this.Titulo = titulo;
        this.Texto = texto;
    }
    public Nota(String titulo, String texto) {
        this.Titulo = titulo;
        this.Texto = texto;
    }
    public Nota(String titulo, String texto,String tipo) {
        this.Titulo = titulo;
        this.Texto = texto;
        this.Tipo = tipo;
    }
    public Nota(String titulo, String texto,String tipo,String fecha, String Hora, String estado) {
        this.Titulo = titulo;
        this.Texto = texto;
        this.Tipo = tipo;
        this.Fecha = fecha;
        this.Hora = Hora;
        this.estado = estado;
    }
    public Nota(int ID, String titulo, String texto, String tipo, String fecha,String Hora,String estado) {
        this.ID = ID;
        this.Titulo = titulo;
        this.Texto = texto;
        this.Tipo = tipo;
        this.Fecha = fecha;
        this.Hora = Hora;
        this.estado=estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getTexto() {
        return Texto;
    }

    public void setTexto(String texto) {
        Texto = texto;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }
}
