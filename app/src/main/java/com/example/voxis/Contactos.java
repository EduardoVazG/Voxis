package com.example.voxis;

public class Contactos {
    private String nombre;
    private String hora_llamada;
    private int perfil_icono;

    public Contactos(String nombre, String hora_llamada, int perfil_icono) {
        this.nombre = nombre;
        this.hora_llamada = hora_llamada;
        this.perfil_icono = perfil_icono;
    }

    public String getName() {
        return nombre;
    }

    public String getCallTime() {
        return hora_llamada;
    }

    public int getProfileImage() {
        return perfil_icono;
    }
}
