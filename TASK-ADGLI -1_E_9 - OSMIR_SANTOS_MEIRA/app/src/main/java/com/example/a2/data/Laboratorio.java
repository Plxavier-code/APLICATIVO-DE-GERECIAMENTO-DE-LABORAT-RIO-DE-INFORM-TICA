package com.example.a2.data;

public class Laboratorio {
    private String id;
    private String nome;
    private String capacidade;
    private String descricao;

    // Construtor vazio (necessário para o Firebase)
    public Laboratorio() {}

    public Laboratorio(String id, String nome, String capacidade, String descricao) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.descricao = descricao;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCapacidade() { return capacidade; }
    public void setCapacidade(String capacidade) { this.capacidade = capacidade; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}