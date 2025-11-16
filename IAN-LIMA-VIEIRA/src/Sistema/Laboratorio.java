package Sistema;

import EstrutrurasDados.ListaEstatica.ListaEstatica;

public class Laboratorio {
    private String nome;
    private StatusLaboratorio status;
    private ListaEstatica<Estacao> estacoes; 

    public Laboratorio(String nome, int capacidadeMaxima) {
        this.nome = nome;
        status = StatusLaboratorio.ABERTO;
        estacoes = new ListaEstatica<>(capacidadeMaxima); 

        for (int id = 1; id <= capacidadeMaxima; id++) {
            Estacao novaEstacao = new Estacao(id); 
            this.estacoes.add(novaEstacao);     
        }
    }

    public void adicionarEstacao(Estacao estacao) {
        if (!estacoes.isFull()) {
            estacoes.add(estacao);
            System.out.println("Estação " + estacao.getId() + " adicionada ao " + this.nome);
        } else {
            System.out.println("Laboratório " + this.nome + " está com a capacidade máxima.");
        }
    }
    
    public String getNome(){
        return nome; 
    }

    public StatusLaboratorio getStatus(){ 
        return status; 
    }

    public ListaEstatica<Estacao> getEstacoes(){ 
        return estacoes; 
    }

    public void setStatus(StatusLaboratorio status) {
        this.status = status;
    }

    
    public Estacao getEstacaoPorId(int id) {
        for(int i = 0; i < estacoes.size(); i++) {
            Estacao e = estacoes.get(i);
            if(e.getId() == id) {
                return e;
            }
        }
        return null; 
    }

    @Override
    public String toString() {
        return "Laboratorio [nome=" + nome + ", status=" + status + ", estacoes=" + estacoes.size() + "/" + estacoes.isFull() + "]";
    }
}
