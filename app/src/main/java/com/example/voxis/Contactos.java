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

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCallTime() { return callTime; }
    public void setCallTime(String callTime) { this.callTime = callTime; }
    public int getProfileImage() { return profileImage; }
    public void setProfileImage(int profileImage) { this.profileImage = profileImage; }
    public String getNumero_contacto() { return numero_contacto; }
    public void setNumero_contacto(String numero_contacto) { this.numero_contacto = numero_contacto; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
