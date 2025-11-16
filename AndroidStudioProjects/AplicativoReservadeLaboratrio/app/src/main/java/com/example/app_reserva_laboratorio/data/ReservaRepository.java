package com.example.app_reserva_laboratorio.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Repositório (Repository) - Simula nosso banco de dados.
 * É a principal classe da "Camada de Dados".
 * Usa o padrão Singleton para garantir que só exista uma lista de reservas.
 */
public class ReservaRepository {

    private static ReservaRepository instance; // O Singleton

    // As "tabelas" do banco de dados
    private List<Laboratorio> laboratorios;
    private List<Reserva> reservas;
    private long proximoIdReserva = 3; // Começa depois das reservas

    // Construtor privado (parte do Singleton)
    private ReservaRepository() {
        laboratorios = new ArrayList<>();
        reservas = new ArrayList<>();
        carregarLaboratoriosIniciais();
        carregarReservasIniciais();
    }

    /**
     * Pega a instância única (Singleton) do repositório.
     * @return A instância de ReservaRepository.
     */
    public static synchronized ReservaRepository getInstance() {
        if (instance == null) {
            instance = new ReservaRepository();
        }
        return instance;
    }

    // Métodos de Laboratório

    /**
     * Retorna a lista de todos os laboratórios cadastrados.
     * @return Lista de Laboratorios.
     */
    public List<Laboratorio> getLaboratorios() {
        return laboratorios;
    }

    /* Método para "traduzir" o ID no Nome.
     * Procura um laboratório pelo seu ID.
     * @param id O ID do laboratório
     * @return O objeto Laboratorio, ou null se não for encontrado.
     */
    public Laboratorio getLaboratorioById(String id) {
        for (Laboratorio lab : laboratorios) {
            if (lab.getId().equals(id)) {
                return lab;
            }
        }
        // Se não achou nos labs, pode ser uma estação
        if ("est_12".equals(id)) {
            return new Laboratorio("est_12", "Estação 12 - LAB - H403", 1, "Prédio H, 4º andar", "icon_estacao");
        }
        return null;
    }

    // Métodos de Reserva (CRUD)

    /**
     * CREATE (Criar): Adiciona uma nova reserva à lista.
     * @param novaReserva O objeto Reserva a ser adicionado.
     */
    public void addReserva(Reserva novaReserva) {
        reservas.add(novaReserva);
        proximoIdReserva++;
    }

    /**
     * READ (Ler): Retorna todas as reservas.
     * @return Lista de todas as Reservas.
     */
    public List<Reserva> getReservas() {
        return reservas;
    }

    /**
     * READ (Ler): Retorna as reservas de um aluno específico.
     * (Usado pela tela "Minhas Reservas")
     * @param alunoId O ID do aluno.
     * @return Lista de Reservas daquele aluno.
     */
    public List<Reserva> getReservasPorAluno(String alunoId) {
        List<Reserva> reservasDoAluno = new ArrayList<>();
        for (Reserva r : reservas) {
            if (r.getAlunoId().equals(alunoId)) {
                reservasDoAluno.add(r);
            }
        }
        return reservasDoAluno;
    }

    /**
     * READ (Ler): Encontra uma reserva pelo seu ID.
     * (Usado pela tela "Editar Reserva")
     * @param idReserva O ID da reserva a ser buscada.
     * @return O objeto Reserva, ou null se não for encontrado.
     */
    public Reserva getReservaById(String idReserva) {
        for (Reserva r : reservas) {
            if (r.getIdReserva().equals(idReserva)) {
                return r;
            }
        }
        return null;
    }

    /**
     * UPDATE (Editar): Atualiza os dados de uma reserva existente.
     * (Usado pela tela "Editar Reserva")
     */
    public void updateReserva(String idReserva, String data, int minutoInicio, int minutoFim, String descricao, String laboratorioId) {
        Reserva reservaParaEditar = getReservaById(idReserva);
        if (reservaParaEditar != null) {
            reservaParaEditar.setData(data);
            reservaParaEditar.setHorarios(minutoInicio, minutoFim);
            reservaParaEditar.setDescricao(descricao);
            reservaParaEditar.setLaboratorioId(laboratorioId);
        }
    }

    /**
     * DELETE (Cancelar): Remove uma reserva da lista.
     * (Usado pela tela "Editar Reserva" -> Botão Cancelar)
     * @param idReserva O ID da reserva a ser removida.
     */
    public void removerReserva(String idReserva) {
        reservas.removeIf(reserva -> reserva.getIdReserva().equals(idReserva));
    }

    // --- Métodos Auxiliares ---

    /**
     * Gera um ID único para uma nova reserva.
     * @return Uma String de ID (ex: "res_3").
     */
    public String getProximoIdReserva() {
        return "res_" + proximoIdReserva;
    }
    // --- Métodos Iniciais (Simulação de Dados) ---

    private void carregarLaboratoriosIniciais() {
        laboratorios.add(new Laboratorio("lab_h401", "LAB - H401", 16, "Prédio H, 4º andar", "icon_lab401"));
        laboratorios.add(new Laboratorio("lab_h402", "LAB - H402", 15, "Prédio H, 4º andar", "icon_lab402"));
        laboratorios.add(new Laboratorio("lab_h403", "LAB - H403", 20, "Prédio H, 4º andar", "icon_lab403"));
        laboratorios.add(new Laboratorio("lab_h404", "LAB - H404", 20, "Prédio H, 4º andar", "icon_lab404"));
        laboratorios.add(new Laboratorio("lab_h406", "LAB - H406", 20, "Prédio H, 4º andar", "icon_lab406"));
        laboratorios.add(new Laboratorio("lab_h407", "LAB - H407", 20, "Prédio H, 4º andar", "icon_lab407"));

        // Estações (Filhas do LAB - H401)
        laboratorios.add(new Laboratorio("est_h401_1", "Estação 1 - LAB - H401", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h401_5", "Estação 5 - LAB - H401", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h401_10", "Estação 10 - LAB - H401", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h401_12", "Estação 12 - LAB - H401", 1, "Prédio H, 4º andar", "icon_estacao"));

        // Estações (Filhas do LAB - H402)
        laboratorios.add(new Laboratorio("est_h402_1", "Estação 1 - LAB - H402", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h402_5", "Estação 5 - LAB - H402", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h402_10", "Estação 10 - LAB - H402", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h402_12", "Estação 12 - LAB - H402", 1, "Prédio H, 4º andar", "icon_estacao"));

        // Estações (Filhas do LAB - H403)
        laboratorios.add(new Laboratorio("est_h403_1", "Estação 1 - LAB - H403", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h403_5", "Estação 5 - LAB - H403", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h403_10", "Estação 10 - LAB - H403", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h403_12", "Estação 12 - LAB - H403", 1, "Prédio H, 4º andar", "icon_estacao"));

        // Estações (Filhas do LAB - H404)
        laboratorios.add(new Laboratorio("est_h404_1", "Estação 1 - LAB - H404", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h404_5", "Estação 5 - LAB - H404", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h404_10", "Estação 10 - LAB - H404", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h404_12", "Estação 12 - LAB - H404", 1, "Prédio H, 4º andar", "icon_estacao"));

        // Estações (Filhas do LAB - H406)
        laboratorios.add(new Laboratorio("est_h406_1", "Estação 1 - LAB - H406", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h406_5", "Estação 5 - LAB - H406", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h406_10", "Estação 10 - LAB - H406", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h406_12", "Estação 12 - LAB - H406", 1, "Prédio H, 4º andar", "icon_estacao"));

        // Estações (Filhas do LAB - H407)
        laboratorios.add(new Laboratorio("est_h407_1", "Estação 1 - LAB - H407", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h407_5", "Estação 5 - LAB - H407", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h407_10", "Estação 10 - LAB - H407", 1, "Prédio H, 4º andar", "icon_estacao"));
        laboratorios.add(new Laboratorio("est_h407_12", "Estação 12 - LAB - H407", 1, "Prédio H, 4º andar", "icon_estacao"));
    }

    private void carregarReservasIniciais() {
        // Simula as reservas que já existem na tela "Minhas Reservas"
        reservas.add(new Reserva("res_0", "lab_h401", "12/11/2025", 990, 1110, "Mini curso", "aluno_teste_123"));
        reservas.add(new Reserva("res_1", "lab_h407", "14/11/2025", 1110, 1320, "Linguagem de Programação II", "aluno_teste_123"));
        reservas.add(new Reserva("res_2", "est_h403_12", "16/11/2025", 1110, 1320, "Estudo", "aluno_teste_123"));    }
}
