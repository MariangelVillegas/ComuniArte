package org.example.model;

public class Usuario {
    private String _id;
    private String nombre;
    private String apellido;
    private String email;
    private int edad;
    private String tipo;
    private String password = "";

    public Usuario() {}
    public Usuario(String nombre, String apellido, String email, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad= edad;
    }

    public Usuario(String _id, String nombre, String apellido, String email, int edad, String tipo, String password) {
        this._id = _id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.edad= edad;
        this.tipo = tipo;
        this.password = password;
    }

    public Usuario(String id_usuario, String nombre, String tipo) {
        this._id = id_usuario;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String get_id() {
        return _id;
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
