package com.example.voxis;

public class Contactos {
    private int id;
    private String name;
    private String callTime;
    private int profileImage;
    private String numero_contacto;
    private String correo;
    private String categoria;

    // Constructor
    public Contactos(int id, String name, String callTime, int profileImage, String numero_contacto, String correo, String categoria) {
        this.id = id;
        this.name = name;
        this.callTime = callTime;
        this.profileImage = profileImage;
        this.numero_contacto = numero_contacto;
        this.correo = correo;
        this.categoria = categoria;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCallTime() {
        return callTime;
    }

    public int getProfileImage() {
        return profileImage;
    }

    public String getNumero_contacto() {
        return numero_contacto;
    }

    public String getCorreo() {
        return correo;
    }

    public String getCategoria() {
        return categoria;
    }
}