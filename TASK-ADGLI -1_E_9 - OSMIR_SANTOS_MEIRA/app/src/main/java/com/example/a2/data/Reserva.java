package com.example.a2.data;

import java.util.Date;

public class Reserva {
    private String id;
    private String professorId;
    private String professorEmail;
    private String laboratorioId;
    private String nomeLaboratorio;
    private Date dataReserva;
    private String dataReservaStr;
    private String horarioBloco;
    private String descricao;
    private String status;

    public Reserva() {} // Necessário para Firebase

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getProfessorEmail() {
        return professorEmail;
    }

    public void setProfessorEmail(String professorEmail) {
        this.professorEmail = professorEmail;
    }

    public String getLaboratorioId() {
        return laboratorioId;
    }

    public void setLaboratorioId(String laboratorioId) {
        this.laboratorioId = laboratorioId;
    }

    public String getNomeLaboratorio() {
        return nomeLaboratorio;
    }

    public void setNomeLaboratorio(String nomeLaboratorio) {
        this.nomeLaboratorio = nomeLaboratorio;
    }

    public Date getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(Date dataReserva) {
        this.dataReserva = dataReserva;
    }

    public String getDataReservaStr() {
        return dataReservaStr;
    }

    public void setDataReservaStr(String dataReservaStr) {
        this.dataReservaStr = dataReservaStr;
    }

    public String getHorarioBloco() {
        return horarioBloco;
    }

    public void setHorarioBloco(String horarioBloco) {
        this.horarioBloco = horarioBloco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}