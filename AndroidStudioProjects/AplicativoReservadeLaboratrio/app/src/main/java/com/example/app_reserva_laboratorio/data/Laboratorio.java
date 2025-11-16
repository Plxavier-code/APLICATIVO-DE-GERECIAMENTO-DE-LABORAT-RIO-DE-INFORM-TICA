package com.example.app_reserva_laboratorio.data;

/**
 * Modelo (Model) que representa um Laboratório.
 * Parte da "Camada de Dados".
 */
public class Laboratorio {
    private String id;
    private String nome;
    private int capacidade;
    private String localizacao;
    private String urlImagem;

    // Construtor
    public Laboratorio(String id, String nome, int capacidade, String localizacao, String urlImagem) {
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
        this.urlImagem = urlImagem;
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
}
