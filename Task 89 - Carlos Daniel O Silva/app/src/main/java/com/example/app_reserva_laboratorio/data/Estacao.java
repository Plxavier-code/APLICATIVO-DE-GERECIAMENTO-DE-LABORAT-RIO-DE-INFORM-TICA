

package com.example.app_reserva_laboratorio.data;

public class Estacao {

    private String idEstacao;
    private String idLabPai; 
    private String nome; 
    private boolean emManutencao;
    private boolean disponivel;

    public Estacao(String idEstacao, String idLabPai, String nome) {
        this.idEstacao = idEstacao;
        this.idLabPai = idLabPai;
        this.nome = nome;
        this.emManutencao = false;
        this.disponivel = true;
    }

    // Getters
    public String getIdEstacao() {
        return idEstacao;
    }

    public String getIdLabPai() {
        return idLabPai;
    }

    public String getNome() {
        return nome;
    }

    public boolean isEmManutencao() {
        return emManutencao;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    
    public void setEmManutencao(boolean emManutencao) {
        this.emManutencao = emManutencao;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}