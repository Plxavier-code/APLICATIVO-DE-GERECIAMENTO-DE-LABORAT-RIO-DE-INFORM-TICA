package com.example.a2.data;

public class Reserva {
    private String idReserva;
    private String laboratorioId; // ID do Lab ou da Estação (ex: "lab_h401" ou "est_1_lab_h401")
    private String data;          // Formato: dd/MM/yyyy
    private int minutoInicio;     // Ex: 19:00 = 1140 minutos
    private int minutoFim;        // Ex: 20:00 = 1200 minutos
    private String descricao;
    private String alunoId;       // ID do usuário que fez a reserva

    public Reserva() {}

    public Reserva(String idReserva, String laboratorioId, String data, int minutoInicio, int minutoFim, String descricao, String alunoId) {
        this.idReserva = idReserva;
        this.laboratorioId = laboratorioId;
        this.data = data;
        this.minutoInicio = minutoInicio;
        this.minutoFim = minutoFim;
        this.descricao = descricao;
        this.alunoId = alunoId;
    }

    // Getters e Setters
    public String getIdReserva() { return idReserva; }
    public void setIdReserva(String idReserva) { this.idReserva = idReserva; }

    public String getLaboratorioId() { return laboratorioId; }
    public void setLaboratorioId(String laboratorioId) { this.laboratorioId = laboratorioId; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public int getMinutoInicio() { return minutoInicio; }
    public void setMinutoInicio(int minutoInicio) { this.minutoInicio = minutoInicio; }

    public int getMinutoFim() { return minutoFim; }
    public void setMinutoFim(int minutoFim) { this.minutoFim = minutoFim; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getAlunoId() { return alunoId; }
    public void setAlunoId(String alunoId) { this.alunoId = alunoId; }

    // Método auxiliar para exibir horário formatado
    public String getHorarioFormatado() {
        int h1 = minutoInicio / 60;
        int m1 = minutoInicio % 60;
        int h2 = minutoFim / 60;
        int m2 = minutoFim % 60;
        return String.format("%02d:%02d - %02d:%02d", h1, m1, h2, m2);
    }
}