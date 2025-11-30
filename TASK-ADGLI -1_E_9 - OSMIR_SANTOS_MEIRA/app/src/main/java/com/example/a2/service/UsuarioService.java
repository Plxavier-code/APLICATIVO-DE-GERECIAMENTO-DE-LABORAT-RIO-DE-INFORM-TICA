package com.example.a2.service;

import com.example.a2.data.Tipo;
import com.example.a2.data.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Simula um serviço de gerenciamento de usuários, como um banco de dados.
 */
public class UsuarioService {

    private static final List<Usuario> usuarios = new ArrayList<>();

    static {
        usuarios.add(new Usuario("1", "Administrador", "admin@if.com", Tipo.ADMINISTRADOR, "TI"));
        usuarios.add(new Usuario("2", "Professor Fulano", "prof@if.com", Tipo.PROFESSOR, "Informática"));
        usuarios.add(new Usuario("3", "Aluno Ciclano", "aluno@if.com", Tipo.ALUNO, "20231INFO001"));
    }

    public Usuario autenticar(String email, String senha) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                String senhaEsperada = "";
                switch (usuario.getTipo()) {
                    case ADMINISTRADOR: senhaEsperada = "admin123"; break;
                    case PROFESSOR: senhaEsperada = "prof123"; break;
                    case ALUNO: senhaEsperada = "aluno123"; break;
                }

                if (senha.equals(senhaEsperada)) {
                    return usuario;
                }
            }
        }
        return null;
    }
}
