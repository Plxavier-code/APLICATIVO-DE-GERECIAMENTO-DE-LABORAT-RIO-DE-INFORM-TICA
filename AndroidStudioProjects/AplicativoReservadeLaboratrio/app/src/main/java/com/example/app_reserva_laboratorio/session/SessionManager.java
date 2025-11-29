package com.example.app_reserva_laboratorio.session;

import com.example.app_reserva_laboratorio.data.Usuario;

/**
 * Singleton para gerenciar a sessão do usuário logado.
 * Em um app real, isso seria inicializado após o login.
 * Por enquanto, simulamos o login de um usuário específico.
 */
public class SessionManager {

    private static SessionManager instance;
    private Usuario usuarioLogado;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public boolean isProfessor() {
        return usuarioLogado != null && "Professor".equalsIgnoreCase(usuarioLogado.getTipo());
    }
}
