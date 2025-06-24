package org.example.service;

import org.example.model.Usuario;
import org.example.repositorio.AuthRepository;
import org.example.repositorio.UsuarioRepositorio;

public class AuthService {

    private UsuarioRepositorio usuarioRepositorio = new UsuarioRepositorio();
    private AuthRepository authRepository = new AuthRepository();

    public Usuario loginUser(String email, String password) {
        Usuario usuario = usuarioRepositorio.getUsuarioByEmail(email);
        if (usuario == null) {
            System.err.println("Usuario no encontrado");
            return null;
        }

        if(!password.equals(usuario.getPassword())) {
            System.err.println("Password incorrecto");
            return null;
        }

        authRepository.saveLoggedUser(usuario);
        return usuario;
    }

    public void signUpUser(Usuario usuario) {
        usuarioRepositorio.guardar(usuario);
    }
}
