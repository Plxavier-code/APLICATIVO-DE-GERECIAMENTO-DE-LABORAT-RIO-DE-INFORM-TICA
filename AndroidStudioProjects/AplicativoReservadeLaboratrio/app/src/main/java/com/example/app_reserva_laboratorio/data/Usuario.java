package com.example.app_reserva_laboratorio.data;

/**
 * Modelo (Model) que representa um Usuário.
 * Agora utiliza um Enum para o tipo de usuário para maior segurança e clareza.
 */
public class Usuario {

    // Enum para definir os tipos de usuário de forma segura
    public enum Tipo {
        ADMINISTRADOR,
        PROFESSOR,
        ALUNO
    }

    private String id;
    private String nome;
    private String email;
    private Tipo tipo; // O tipo de usuário agora é um Enum
    private String identificador; // Matrícula (Aluno) ou Departamento (Professor)

    public Usuario(String id, String nome, String email, Tipo tipo, String identificador) {
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

    public Tipo getTipo() {
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
