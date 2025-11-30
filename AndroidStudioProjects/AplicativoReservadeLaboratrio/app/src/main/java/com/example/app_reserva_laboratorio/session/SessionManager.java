package com.example.app_reserva_laboratorio.session;

import com.example.app_reserva_laboratorio.data.Tipo;
import com.example.app_reserva_laboratorio.data.Usuario;

/**
 * Singleton para gerenciar a sessão do usuário logado.
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

    /**
     * Verifica se o usuário logado é um professor.
     * @return true se o usuário logado for um professor, false caso contrário.
     */
    public boolean isProfessor() {
        return usuarioLogado != null && usuarioLogado.getTipo() == Tipo.PROFESSOR;
    }
}
