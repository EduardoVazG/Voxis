package com.example.voxis;

public class Contactos {
    private String nombre;
    private String hora_llamada;
    private String numero_contacto;
    private int perfil_icono;

    public Contactos(String nombre, String hora_llamada, int perfil_icono, String numero_contacto) {
        this.nombre = nombre;
        this.hora_llamada = hora_llamada;
        this.perfil_icono = perfil_icono;
        this.numero_contacto = numero_contacto;
    }

    public String getName() {
        return nombre;
    }

    public String getCallTime() {
        return hora_llamada;
    }

    public String getNumero_contacto() {
        return numero_contacto;
    }

    public int getProfileImage() {
        return perfil_icono;
    }
}
