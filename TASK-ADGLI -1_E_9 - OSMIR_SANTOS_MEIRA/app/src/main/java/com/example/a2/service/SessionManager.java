package com.example.a2.service;

import com.example.a2.data.Usuario;

/**
 * Gerencia a sessão do usuário logado (Singleton).
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

    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public void logout() {
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean isUserLoggedIn() {
        return usuarioLogado != null;
    }
}
