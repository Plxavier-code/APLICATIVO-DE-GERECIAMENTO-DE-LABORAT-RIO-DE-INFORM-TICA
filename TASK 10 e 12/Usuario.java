public class Usuario {
    private String id;
    private String nome;
    private String tipo;

    public Usuario(String id, String nome, String tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo.toLowerCase();
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
}
