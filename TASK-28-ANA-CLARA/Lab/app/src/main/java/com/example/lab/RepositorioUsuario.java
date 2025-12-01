package com.example.lab;

import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuario {
    private static volatile RepositorioUsuario instancia;
    private List<Usuario> usuarios = new ArrayList<>();

    private RepositorioUsuario() {
        // Adiciona alguns usuários padrão para teste. Para criar cadastro como adiministrador, o final do email deve conter "@admin.com"
        usuarios.add(new Usuario("Admin", "admin@admin.com", "admin123", Perfil.ADMINISTRADOR));
        usuarios.add(new Usuario("Professor", "professor@lab.com", "prof123", Perfil.PROFESSOR));
        usuarios.add(new Usuario("Aluno", "aluno@lab.com", "aluno123", Perfil.ALUNO));
    }

    public static RepositorioUsuario getInstancia() {
        if (instancia == null) {
            instancia = new RepositorioUsuario();
        }
        return instancia;
    }

    public Usuario encontrarUsuarioPorEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
}
