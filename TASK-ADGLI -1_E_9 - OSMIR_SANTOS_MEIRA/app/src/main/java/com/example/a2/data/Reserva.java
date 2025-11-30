package com.example.a2.data;

public class Reserva {
    private String idReserva;
    private String laboratorioId;
    private String data;
    private int minutoInicio;
    private int minutoFim;
    private String descricao;
    private String alunoId;

    public Reserva(String idReserva, String laboratorioId, String data, int minutoInicio, int minutoFim, String descricao, String alunoId) {
        this.idReserva = idReserva;
        this.laboratorioId = laboratorioId;
        this.data = data;
        this.minutoInicio = minutoInicio;
        this.minutoFim = minutoFim;
        this.descricao = descricao;
        this.alunoId = alunoId;
    }

    // Getters
    public String getIdReserva() {
        return idReserva;
    }
    public String getLaboratorioId() {
        return laboratorioId;
    }
    public String getData() {
        return data;
    }
    public int getMinutoInicio() {
        return minutoInicio;
    }
    public int getMinutoFim() {
        return minutoFim;
    }
    public String getAlunoId() {
        return alunoId;
    }
    public String getDescricao() {
        return descricao;
    }

    public String getHorarioFormatado() {
        int h_ini = minutoInicio / 60;
        int m_ini = minutoInicio % 60;
        int h_fim = minutoFim / 60;
        int m_fim = minutoFim % 60;
        return String.format("%02d:%02d - %02d:%02d", h_ini, m_ini, h_fim, m_fim);
    }

    // Setters
    public void setData(String data) {
        this.data = data;
    }
    public void setLaboratorioId(String laboratorioId) {
        this.laboratorioId = laboratorioId;
    }
    public void setHorarios(int minutoInicio, int minutoFim) {
        this.minutoInicio = minutoInicio;
        this.minutoFim = minutoFim;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
