package com.example.a2.data;

public class Laboratorio {
    private String id;
    private String nome;
    private int capacidade;
    private String localizacao;
    private String urlImagem;
    private boolean emManutencao;
    private boolean disponivel;

    public Laboratorio(String id, String nome, int capacidade, String localizacao, String urlImagem) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
        this.urlImagem = urlImagem;
        this.emManutencao = false;
        this.disponivel = true;
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public int getCapacidade() {
        return capacidade;
    }
    public String getLocalizacao() {
        return localizacao;
    }
    public String getUrlImagem() {
        return urlImagem;
    }
    public boolean isEmManutencao() {
        return emManutencao;
    }
    public boolean isDisponivel() {
        return disponivel;
    }

    // Setters
    public void setEmManutencao(boolean emManutencao) {
        this.emManutencao = emManutencao;
    }
    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }
}
