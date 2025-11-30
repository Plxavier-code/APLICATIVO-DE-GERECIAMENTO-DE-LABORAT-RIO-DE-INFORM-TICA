package com.example.app_reserva_laboratorio.service;

import com.example.app_reserva_laboratorio.data.Tipo; // IMPORT CORRIGIDO
import com.example.app_reserva_laboratorio.data.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Simula um serviço de gerenciamento de usuários, como um banco de dados.
 */
public class UsuarioService {

    private static final List<Usuario> usuarios = new ArrayList<>();

    // Bloco estático para popular nossa "base de dados" de usuários de exemplo.
    static {
        // USO CORRIGIDO DO ENUM
        usuarios.add(new Usuario("1", "Administrador", "admin@if.com", Tipo.ADMINISTRADOR, "TI"));
        usuarios.add(new Usuario("2", "Professor Fulano", "prof@if.com", Tipo.PROFESSOR, "Informática"));
        usuarios.add(new Usuario("3", "Aluno Ciclano", "aluno@if.com", Tipo.ALUNO, "20231INFO001"));
    }

    /**
     * Tenta autenticar um usuário com base no e-mail e senha.
     * As senhas são fixas no código apenas para esta simulação.
     * @return O objeto Usuario se a autenticação for bem-sucedida, ou null caso contrário.
     */
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
                    return usuario; // Sucesso!
                }
            }
        }
        return null; // Falha
    }
}
