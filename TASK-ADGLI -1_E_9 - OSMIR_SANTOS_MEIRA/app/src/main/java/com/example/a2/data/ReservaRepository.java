package com.example.a2.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Padrão SINGLETON.
 * Substitui o Firebase. Mantém os dados na memória RAM.
 */
public class ReservaRepository {

    private static ReservaRepository instance;

    private List<Laboratorio> laboratorios;
    private List<Reserva> reservas;
    private List<Usuario> usuarios;
    private long contadorId = 100; // Começa do 100 para IDs fictícios

    private ReservaRepository() {
        laboratorios = new ArrayList<>();
        reservas = new ArrayList<>();
        usuarios = new ArrayList<>();
        inicializarDados();
    }

    public static synchronized ReservaRepository getInstance() {
        if (instance == null) {
            instance = new ReservaRepository();
        }
        return instance;
    }

    // --- LEITURA ---
    public List<Laboratorio> getLaboratorios() {
        return laboratorios;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public List<Reserva> getReservasPorUsuario(String userId) {
        List<Reserva> resultado = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getAlunoId().equals(userId)) {
                resultado.add(r);
            }
        }
        return resultado;
    }

    public Laboratorio getLaboratorioById(String id) {
        for (Laboratorio lab : laboratorios) {
            if (lab.getId().equals(id)) return lab;
        }
        // Se for estação, cria um dummy para não quebrar a lógica
        if (id.startsWith("est_")) {
            return new Laboratorio(id, "Estação de Trabalho", 1, "Lab", "ic_desktop");
        }
        return null;
    }

    // --- ESCRITA ---
    public void adicionarReserva(Reserva nova) {
        reservas.add(nova);
        contadorId++;
    }

    public void removerReserva(String idReserva) {
        reservas.removeIf(r -> r.getIdReserva().equals(idReserva));
    }

    public String gerarNovoId() {
        return "res_" + contadorId;
    }

    // --- DADOS FICTÍCIOS ---
    private void inicializarDados() {
        // Adiciona Labs
        laboratorios.add(new Laboratorio("lab_h401", "LAB - H401", 16, "Prédio H", "icon_lab401"));
        laboratorios.add(new Laboratorio("lab_h402", "LAB - H402", 15, "Prédio H", "icon_lab402"));
        laboratorios.add(new Laboratorio("lab_h403", "LAB - H403", 20, "Prédio H", "icon_lab403"));
        laboratorios.add(new Laboratorio("lab_h404", "LAB - H404", 20, "Prédio H", "icon_lab404"));
        laboratorios.add(new Laboratorio("lab_h406", "LAB - H406", 20, "Prédio H", "icon_lab406"));
        laboratorios.add(new Laboratorio("lab_h407", "LAB - H407", 20, "Prédio H", "icon_lab407"));

        // Adiciona Usuário Padrão
        usuarios.add(new Usuario("user_padrao", "Osmir Santos", "osmir@ifba.edu.br", "Aluno"));

        // Reserva de exemplo
        reservas.add(new Reserva("res_001", "lab_h401", "01/01/2025", 600, 720, "Teste Inicial", "user_padrao"));
    }
}