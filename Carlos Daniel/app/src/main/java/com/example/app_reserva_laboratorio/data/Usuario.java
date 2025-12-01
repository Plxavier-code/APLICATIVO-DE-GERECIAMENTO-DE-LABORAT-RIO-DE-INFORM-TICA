package com.example.app_reserva_laboratorio.data;

/**
 * Modelo (Model) que representa um Usuário (Aluno ou Professor).
 * Baseado no diagrama UML.
 */
public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String tipo; // "Aluno" ou "Professor"
    private String identificador; // Guarda a Matrícula (se Aluno) ou Depto (se Professor)

    public Usuario(String id, String nome, String email, String tipo, String identificador) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.identificador = identificador;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTipo() {
        return tipo;
    }

    public String getIdentificador() {
        return identificador;
    }

    // Setters (Para o Admin poder gerenciar)
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}