
import java.time.LocalDateTime;
import java.time.Duration;

public class Sessao {
    private Usuario usuario;
    private String estacao;
    private LocalDateTime inicio;
    private LocalDateTime fim;

    public Sessao(Usuario usuario, String estacao) {
        this.usuario = usuario;
        this.estacao = estacao;
        this.inicio = LocalDateTime.now();
    }

    public void finalizar() {
        this.fim = LocalDateTime.now();
    }

    public long getDuracaoEmMinutos() {
        if (inicio != null && fim != null) {
            return Duration.between(inicio, fim).toMinutes();
        }
        return 0;
    }

    public Usuario getUsuario() { return usuario; }
    public String getEstacao() { return estacao; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFim() { return fim; }

    @Override
    public String toString() {
        return "Sessão {" +
                "Usuário='" + usuario.getNome() + '\'' +
                ", Estação='" + estacao + '\'' +
                ", Início=" + inicio +
                ", Fim=" + fim +
                ", Duração=" + getDuracaoEmMinutos() + " min" +
                '}';
    }
}
