package Sistema;

import EstrutrurasDados.ListaEstatica.ListaEstatica;

public class Estacao {
    private int id; 
    private StatusEstacao status;
    private ListaEstatica<String> historicoManutencao; 

    public Estacao(int id) {
        this.id = id;
        status = StatusEstacao.DISPONIVEL;
        historicoManutencao = new ListaEstatica<>();
    }

    public int getId(){ 
        return id; 
    }

    public StatusEstacao getStatus(){ 
        return status; 
    }

    public ListaEstatica<String> getHistoricoManutencao(){ 
        return historicoManutencao; 
    }
    
    public void setStatus(StatusEstacao status){
        this.status = status;
    }

    public void registrarManutencao(String descricao) {

        status = StatusEstacao.EM_MANUTENCAO;
        historicoManutencao.add("Data: [HOJE] - " + descricao);
        System.out.println("Manutenção registrada para Estação " + id);
    }
    
    @Override
    public String toString() {
        return "Estacao [id=" + id + ", status=" + status + "]";
    }
}
