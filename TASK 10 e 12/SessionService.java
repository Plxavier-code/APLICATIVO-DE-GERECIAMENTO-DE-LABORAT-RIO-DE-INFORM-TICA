import java.util.*;

public class SessionService {
    private Map<String, Sessao> sessoesAtivas = new HashMap<>();
    private final Map<String, Sessao> sessaoPorUsuario = new HashMap<>();
    private final List<Sessao> historico = new ArrayList<>();

    private Map<String, LinkedList<FilaDeEspera>> filasDeEspera = new HashMap<>();
    private Map<String, FilaDeEspera> filaPorUsuario = new HashMap<>();
    private List<FilaDeEspera> historicoFila = new ArrayList<>();

    public void iniciarSessao(Usuario usuario, String estacao) {
        if (sessaoPorUsuario.containsKey(usuario.getId())) {
            System.out.println("❌ " + usuario.getNome() + " já possui uma sessão ativa.");
            return;
        }
        if (filaPorUsuario.containsKey(usuario.getId())) {
             System.out.println("❌ " + usuario.getNome() + " já está na fila para a estação " + filaPorUsuario.get(usuario.getId()).getEstacao() + ". Aguarde ou saia da fila para entrar em outra estação.");
             exibirPosicao(usuario);
             return;
        }

        // 2. A Estação está ocupada?
        if (sessoesAtivas.containsKey(estacao)) {
            // Se estiver ocupada, entra na fila.
            entrarNaFila(usuario, estacao);
            return;
        }
        
        LinkedList<FilaDeEspera> fila = filasDeEspera.get(estacao);
        if (fila != null && !fila.isEmpty()) {
            FilaDeEspera proximo = fila.peek();
            
            if (!proximo.getUsuario().getId().equals(usuario.getId())) {
                System.out.println("⚠️ Estação " + estacao + " está livre, mas há usuários na fila de espera.");
                System.out.println("   Prioridade: **" + proximo.getUsuario().getNome() + "** é o próximo. Você deve entrar na fila.");
                
                entrarNaFila(usuario, estacao);
                return;
            } else {
                removerDaFila(usuario);
                System.out.println("🎉 " + usuario.getNome() + " removido da fila. Sua vez chegou na estação " + estacao + ".");
            }
        }
        
        Sessao nova = new Sessao(usuario, estacao);
        sessoesAtivas.put(estacao, nova);
        sessaoPorUsuario.put(usuario.getId(), nova);
        historico.add(nova);
        System.out.println("✔ Sessão iniciada na estação " + estacao + " por " + usuario.getNome());
    }

    public void finalizarSessao(Usuario usuario) {
        Sessao sessao = sessaoPorUsuario.get(usuario.getId());
        if (sessao == null) {
            System.out.println("❌ " + usuario.getNome() + " não possui sessão ativa.");
            return;
        }
        sessao.finalizar();
        String estacaoLiberada = sessao.getEstacao();
        
        sessoesAtivas.remove(estacaoLiberada);
        sessaoPorUsuario.remove(usuario.getId());
        
        System.out.println("✔ Sessão finalizada para " + usuario.getNome());
        System.out.println("   Tempo total: " + sessao.getDuracaoEmMinutos() + " minutos");
        
        notificarProximo(estacaoLiberada);
    }
    
    public void entrarNaFila(Usuario usuario, String estacao) {
        if (filaPorUsuario.containsKey(usuario.getId())) {
            System.out.println("❌ " + usuario.getNome() + " já está na fila para a estação " + filaPorUsuario.get(usuario.getId()).getEstacao() + ".");
            exibirPosicao(usuario);
            return;
        }
        if (sessaoPorUsuario.containsKey(usuario.getId())) {
            System.out.println("❌ " + usuario.getNome() + " já possui uma sessão ativa. Não pode entrar na fila.");
            return;
        }

        filasDeEspera.putIfAbsent(estacao, new LinkedList<>());

        FilaDeEspera novaEntrada = new FilaDeEspera(usuario, estacao);
        filasDeEspera.get(estacao).add(novaEntrada);
        filaPorUsuario.put(usuario.getId(), novaEntrada);
        historicoFila.add(novaEntrada); 
        
        System.out.println("⏳ " + usuario.getNome() + " entrou na fila para a estação " + estacao + ".");
        exibirPosicao(usuario);
    }
    
    private void removerDaFila(Usuario usuario) {
        FilaDeEspera entrada = filaPorUsuario.get(usuario.getId());
        if (entrada != null) {
            String estacao = entrada.getEstacao();
            filasDeEspera.get(estacao).remove(entrada);
            filaPorUsuario.remove(usuario.getId());
        }
    }
    
    public void sairDaFila(Usuario usuario) {
        FilaDeEspera entrada = filaPorUsuario.get(usuario.getId());
        if (entrada == null) {
            System.out.println("❌ " + usuario.getNome() + " não está em nenhuma fila de espera.");
            return;
        }

        String estacao = entrada.getEstacao();
        
        boolean eraPrimeiro = filasDeEspera.get(estacao).peek() != null && filasDeEspera.get(estacao).peek().getUsuario().getId().equals(usuario.getId());

        removerDaFila(usuario);
        System.out.println("✔ " + usuario.getNome() + " saiu da fila para a estação " + estacao + ".");
        
        if (eraPrimeiro) {
            notificarProximo(estacao);
        }
    }
    
    public void exibirPosicao(Usuario usuario) {
        FilaDeEspera entrada = filaPorUsuario.get(usuario.getId());
        if (entrada == null) {
            return;
        }
        
        String estacao = entrada.getEstacao();
        LinkedList<FilaDeEspera> fila = filasDeEspera.get(estacao);
        
        int posicao = fila.indexOf(entrada) + 1;
        
        System.out.println("➡ " + usuario.getNome() + ", sua posição na fila para a estação " + estacao + " é: " + posicao + "/" + fila.size());
    }
    
    private void notificarProximo(String estacao) {
        LinkedList<FilaDeEspera> fila = filasDeEspera.get(estacao);
        
        if (fila != null && !fila.isEmpty()) {
            FilaDeEspera proximo = fila.peek();
            System.out.println("🔔 NOTIFICAÇÃO: Estação " + estacao + " está **LIVRE**! É a vez de **" + proximo.getUsuario().getNome() + "**.");
            System.out.println("   " + proximo.getUsuario().getNome() + ", inicie a sessão imediatamente para garantir seu lugar.");
        }
    }
    
    public void listarHistorico() {
        System.out.println("\n📜 Histórico de Sessões:");
        for (Sessao s : historico) {
            System.out.println(s);
        }
        System.out.println("\n⏳ Histórico da Fila de Espera:");
        for (FilaDeEspera fe : historicoFila) {
            System.out.println(fe);
        }
    }

    public Map<String, Sessao> getSessoesAtivas() {
        return sessoesAtivas;
    }

    public void setSessoesAtivas(Map<String, Sessao> sessoesAtivas) {
        this.sessoesAtivas = sessoesAtivas;
    }

}
