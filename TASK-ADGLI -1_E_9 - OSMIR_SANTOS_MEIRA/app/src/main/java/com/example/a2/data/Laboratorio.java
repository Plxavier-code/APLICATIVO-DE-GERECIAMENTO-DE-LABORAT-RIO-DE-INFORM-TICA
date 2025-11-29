package com.example.a2.data;

public class Laboratorio {
    private String id;
    private String nome;
    private int capacidade;
    private String localizacao;
    private String urlImagem; // Nome do recurso drawable (ex: "icon_lab401")

    public Laboratorio() {}

    public Laboratorio(String id, String nome, int capacidade, String localizacao, String urlImagem) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
        this.urlImagem = urlImagem;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getCapacidade() { return capacidade; }
    public void setCapacidade(int capacidade) { this.capacidade = capacidade; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }
}