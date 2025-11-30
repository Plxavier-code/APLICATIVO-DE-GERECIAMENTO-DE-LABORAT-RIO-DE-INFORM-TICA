package com.example.app_reserva_laboratorio.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Repositório (Repository) - Simula nosso banco de dados.
 * Utiliza Maps para busca otimizada (O(1)) por ID.
 */
public class ReservaRepository {

    private static ReservaRepository instance;

    // ESTRUTURA OTIMIZADA: Usando Map para busca em O(1)
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

    // OTIMIZADO: Busca em O(1)
    public Laboratorio getLaboratorioById(String id) {
        return laboratoriosMap.get(id);
    }

    // OTIMIZADO: Atualização em O(1)
    public void updateLaboratorio(Laboratorio labAtualizado) {
        if (labAtualizado != null && laboratoriosMap.containsKey(labAtualizado.getId())) {
            laboratoriosMap.put(labAtualizado.getId(), labAtualizado);
        }
    }

    // --- Métodos de Reserva (CRUD) ---

    // OTIMIZADO: Inserção em O(1)
    public void addReserva(Reserva novaReserva) {
        reservasMap.put(novaReserva.getIdReserva(), novaReserva);
        proximoIdReserva++;
    }

    public List<Reserva> getReservas() {
        return new ArrayList<>(reservasMap.values());
    }

    // Complexidade O(n) mantida, pois a busca não é por ID único
    public List<Reserva> getReservasPorAluno(String alunoId) {
        return reservasMap.values().stream()
                .filter(r -> r.getAlunoId().equals(alunoId))
                .collect(Collectors.toList());
    }

    // OTIMIZADO: Busca em O(1)
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

    // OTIMIZADO: Remoção em O(1)
    public void removerReserva(String idReserva) {
        reservasMap.remove(idReserva);
    }

    // --- Métodos de Usuário (CRUD - Admin) ---

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuariosMap.values());
    }

    // OTIMIZADO: Remoção em O(1)
    public void removerUsuario(String idUsuario) {
        usuariosMap.remove(idUsuario);
    }

    // OTIMIZADO: Atualização em O(1)
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
        addLaboratorio(new Laboratorio("lab_h403", "LAB - H403", 20, "Prédio H, 4º andar", "icon_lab403"));
        addLaboratorio(new Laboratorio("lab_h404", "LAB - H404", 20, "Prédio H, 4º andar", "icon_lab404"));
        addLaboratorio(new Laboratorio("lab_h406", "LAB - H406", 20, "Prédio H, 4º andar", "icon_lab406"));
        addLaboratorio(new Laboratorio("lab_h407", "LAB - H407", 20, "Prédio H, 4º andar", "icon_lab407"));

        addLaboratorio(new Laboratorio("est_h401_1", "Estação 1 - LAB - H401", 1, "Prédio H, 4º andar", "icon_estacao"));
        addLaboratorio(new Laboratorio("est_h401_5", "Estação 5 - LAB - H401", 1, "Prédio H, 4º andar", "icon_estacao"));
        // ... (demais estações) ...
    }

    private void carregarReservasIniciais() {
        addReserva(new Reserva("res_0", "lab_h401", "12/11/2025", 990, 1110, "Mini curso", "aluno_teste_123"));
        addReserva(new Reserva("res_1", "lab_h407", "14/11/2025", 1110, 1320, "Linguagem de Programação II", "aluno_teste_123"));
        addReserva(new Reserva("res_2", "est_h403_12", "16/11/2025", 1110, 1320, "Estudo", "aluno_teste_123"));
    }

    private void carregarUsuariosIniciais() {
        addUsuario(new Usuario("aluno_teste_123", "Aluno Teste", "aluno@ifba.edu.br", "Aluno", "2022123456"));
        addUsuario(new Usuario("prof_teste_456", "Professor Teste", "prof@ifba.edu.br", "Professor", "Sistemas de Informação"));
        addUsuario(new Usuario("aluno_outro_789", "Outra Aluna", "outra@ifba.edu.br", "Aluno", "2021987654"));
    }

    // Métodos helper para popular os mapas
    private void addLaboratorio(Laboratorio laboratorio) {
        laboratoriosMap.put(laboratorio.getId(), laboratorio);
    }
    private void addUsuario(Usuario usuario) {
        usuariosMap.put(usuario.getId(), usuario);
    }
}
