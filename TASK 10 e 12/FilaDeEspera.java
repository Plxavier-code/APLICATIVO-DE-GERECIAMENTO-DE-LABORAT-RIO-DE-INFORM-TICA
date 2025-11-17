import java.time.LocalDateTime;

public class FilaDeEspera {
    private Usuario usuario;
    private final String estacao;
    private LocalDateTime entrada;

    public FilaDeEspera(Usuario usuario, String estacao) {
        this.usuario = usuario;
        this.estacao = estacao;
        this.entrada = LocalDateTime.now();
    }

    public FilaDeEspera(String estacao, Usuario usuario) {
        this.estacao = estacao;
        this.usuario = usuario;
    }

    public Usuario getUsuario() { return usuario; }
    public String getEstacao() { return estacao; }
    public LocalDateTime getEntrada() { return entrada; }

    @Override
    public String toString() {
        return "Fila {" +
                "Usuário='" + usuario.getNome() + '\'' +
                ", Estação='" + estacao + '\'' +
                ", Entrada=" + entrada +
                '}';
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
