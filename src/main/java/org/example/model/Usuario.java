package org.example.model;

public class Usuario {
    private String id_usuario;
    private String nombre;
    private String apellido;
    private String email;
    private int edad;
    private String tipo;
    private String password = "";

    public Usuario(String id_usuario, String nombre, String apellido,String email, int edad) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad= edad;
    }

    public Usuario(String id_usuario, String nombre, String apellido,String email, int edad, String tipo, String password) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad= edad;
        this.tipo = tipo;
        this.password = password;
    }


    public String getId_usuario() {
        return id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
    public String getEmail() {
        return email;
    }
    public int getEdad() {
        return edad;
    }

    public String getTipo() {
        return tipo;
    }
    public String getPassword() {return password;}
}
