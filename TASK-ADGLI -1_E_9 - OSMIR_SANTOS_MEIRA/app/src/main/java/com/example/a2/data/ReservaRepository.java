package com.example.a2.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReservaRepository {

    private static ReservaRepository instance;

    private final Map<String, Laboratorio> laboratoriosMap;
    private final Map<String, Reserva> reservasMap;
    private final Map<String, Usuario> usuariosMap;
    private long proximoIdReserva = 3;

    private ReservaRepository() {
        laboratoriosMap = new HashMap<>();
        reservasMap = new HashMap<>();
        usuariosMap = new HashMap<>();
        
        carregarLaboratoriosIniciais();
        carregarReservasIniciais();
        carregarUsuariosIniciais();
    }

    public static synchronized ReservaRepository getInstance() {
        if (instance == null) {
            instance = new ReservaRepository();
        }
        return instance;
    }

    // --- Métodos de Laboratório ---

    public List<Laboratorio> getLaboratorios() {
        return new ArrayList<>(laboratoriosMap.values());
    }

    public Laboratorio getLaboratorioById(String id) {
        return laboratoriosMap.get(id);
    }

    public void updateLaboratorio(Laboratorio labAtualizado) {
        if (labAtualizado != null && laboratoriosMap.containsKey(labAtualizado.getId())) {
            laboratoriosMap.put(labAtualizado.getId(), labAtualizado);
        }
    }

    // --- Métodos de Reserva (CRUD) ---

    public void addReserva(Reserva novaReserva) {
        reservasMap.put(novaReserva.getIdReserva(), novaReserva);
        proximoIdReserva++;
    }

    public List<Reserva> getReservas() {
        return new ArrayList<>(reservasMap.values());
    }

    public List<Reserva> getReservasPorAluno(String alunoId) {
        return reservasMap.values().stream()
                .filter(r -> r.getAlunoId().equals(alunoId))
                .collect(Collectors.toList());
    }

    public Reserva getReservaById(String idReserva) {
        return reservasMap.get(idReserva);
    }

    public void updateReserva(String idReserva, String data, int minutoInicio, int minutoFim, String descricao, String laboratorioId) {
        Reserva reservaParaEditar = getReservaById(idReserva);
        if (reservaParaEditar != null) {
            reservaParaEditar.setData(data);
            reservaParaEditar.setHorarios(minutoInicio, minutoFim);
            reservaParaEditar.setDescricao(descricao);
            reservaParaEditar.setLaboratorioId(laboratorioId);
        }
    }

    public void removerReserva(String idReserva) {
        reservasMap.remove(idReserva);
    }

    // --- Métodos de Usuário (CRUD - Admin) ---

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuariosMap.values());
    }

    public void removerUsuario(String idUsuario) {
        usuariosMap.remove(idUsuario);
    }

    public void updateUsuario(Usuario usuarioAtualizado) {
        if (usuarioAtualizado != null && usuariosMap.containsKey(usuarioAtualizado.getId())) {
            usuariosMap.put(usuarioAtualizado.getId(), usuarioAtualizado);
        }
    }

    // --- Métodos Auxiliares ---

    public String getProximoIdReserva() {
        return "res_" + proximoIdReserva;
    }
    
    // --- Métodos Iniciais (Simulação de Dados) ---

    private void carregarLaboratoriosIniciais() {
        addLaboratorio(new Laboratorio("lab_h401", "LAB - H401", 16, "Prédio H, 4º andar", "icon_lab401"));
        addLaboratorio(new Laboratorio("lab_h402", "LAB - H402", 15, "Prédio H, 4º andar", "icon_lab402"));
        addLaboratorio(new Laboratorio("est_h401_1", "Estação 1 - LAB - H401", 1, "Prédio H, 4º andar", "icon_estacao"));
    }

    private void carregarReservasIniciais() {
        addReserva(new Reserva("res_0", "lab_h401", "12/11/2025", 990, 1110, "Mini curso", "aluno_teste_123"));
        addReserva(new Reserva("res_1", "lab_h402", "14/11/2025", 1110, 1320, "Linguagem de Programação II", "prof_teste_456"));
        addReserva(new Reserva("res_2", "est_h401_1", "16/11/2025", 1110, 1320, "Estudo", "aluno_teste_123"));
    }

    private void carregarUsuariosIniciais() {
        addUsuario(new Usuario("aluno_teste_123", "Aluno Teste", "aluno@if.com", Tipo.ALUNO, "2022123456"));
        addUsuario(new Usuario("prof_teste_456", "Professor Teste", "prof@if.com", Tipo.PROFESSOR, "Sistemas de Informação"));
    }

    private void addLaboratorio(Laboratorio laboratorio) {
        laboratoriosMap.put(laboratorio.getId(), laboratorio);
    }
    private void addUsuario(Usuario usuario) {
        usuariosMap.put(usuario.getId(), usuario);
    }
}
