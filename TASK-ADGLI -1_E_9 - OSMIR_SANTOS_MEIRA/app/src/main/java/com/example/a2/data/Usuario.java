package com.example.a2.data;

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String tipo;

    public Usuario(String id, String nome, String email, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTipo() { return tipo; }
}