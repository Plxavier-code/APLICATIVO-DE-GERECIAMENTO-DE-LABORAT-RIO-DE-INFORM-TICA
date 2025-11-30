package com.example.app_reserva_laboratorio.service;

import com.example.app_reserva_laboratorio.data.Usuario;

/**
 * Gerencia a sessão do usuário logado (Singleton).
 * Apenas uma instância desta classe existirá em todo o app.
 */
public class SessionManager {

    private static SessionManager instance;
    private Usuario usuarioLogado;

    // Construtor privado para impedir a criação de novas instâncias
    private SessionManager() {}

    /**
     * Retorna a única instância da classe.
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * Inicia a sessão de um usuário.
     */
    public void login(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    /**
     * Finaliza a sessão do usuário.
     */
    public void logout() {
        this.usuarioLogado = null;
    }

    /**
     * Retorna o objeto do usuário atualmente logado.
     * @return O objeto Usuario ou null se ninguém estiver logado.
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    /**
     * Verifica se há um usuário logado no sistema.
     */
    public boolean isUserLoggedIn() {
        return usuarioLogado != null;
    }
}
