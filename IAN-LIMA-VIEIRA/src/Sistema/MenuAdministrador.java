package Sistema;

import EstrutrurasDados.ListaEstatica.ListaEstatica;
import java.util.Scanner;


public class MenuAdministrador {

    // O sistema principal vai guardar a lista de TODOS os laboratórios
    // Usamos a ListaEncadeada pois podemos ter N laboratórios.
    private ListaEstatica<Laboratorio> todosOsLaboratorios;

    public MenuAdministrador() {
        this.todosOsLaboratorios = new ListaEstatica<>();
        // Vamos adicionar um lab de exemplo
        todosOsLaboratorios.add(new Laboratorio("LAB-01", 20)); // Lab 1 com 20 máquinas
    }

    public void iniciar(Scanner sc) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n------ PAINEL DE ADMINISTRADOR ------");
            System.out.println("1. Gerenciar Laboratórios");
            System.out.println("2. Gerenciar Estações");
            System.out.println("0. Deslogar");
            System.out.print("Escolha: ");
            
            opcao = sc.nextInt();
            sc.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1:
                    menuLaboratorios(sc);
                    break;
                case 2:
                    menuEstacoes(sc);
                    break;
                case 0:
                    System.out.println("Deslogando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    // --- Sub-menu para Laboratórios ---
    private void menuLaboratorios(Scanner sc) {
        System.out.println("\n-- Gerenciar Laboratórios --");
        System.out.println("1. Listar todos os Laboratórios");
        System.out.println("2. Bloquear Laboratório");
        System.out.println("3. Desbloquear Laboratório");
        // ... (você pode adicionar "Cadastrar Novo Laboratório" aqui)
        System.out.print("Escolha: ");
        int op = sc.nextInt();
        sc.nextLine();

        if (op == 1) {
            listarLaboratorios();
        } else if (op == 2 || op == 3) {
            // 3. Implementar funcionalidade de bloqueio
            System.out.print("Digite o nome do Laboratório (ex: LAB-01): ");
            String nome = sc.nextLine();
            Laboratorio lab = buscarLaboratorio(nome);
            
            if (lab != null) {
                StatusLaboratorio novoStatus = (op == 2) ? StatusLaboratorio.BLOQUEADO : StatusLaboratorio.ABERTO;
                lab.setStatus(novoStatus);
                System.out.println(lab.getNome() + " agora está " + lab.getStatus());
            } else {
                System.out.println("Laboratório não encontrado.");
            }
        }
    }

    // --- Sub-menu para Estações ---
    private void menuEstacoes(Scanner sc) {
        System.out.println("\n-- Gerenciar Estações --");
        System.out.println("1. Listar Estações de um Laboratório");
        System.out.println("2. Registrar Manutenção (Corretiva/Preventiva)");
        System.out.println("3. Bloquear Estação");
        System.out.println("4. Liberar Estação");
        // ... (você pode adicionar "Cadastrar Nova Estação" aqui)
        System.out.print("Escolha: ");
        int op = sc.nextInt();
        sc.nextLine();

        System.out.print("Digite o nome do Laboratório (ex: LAB-01): ");
        String nomeLab = sc.nextLine();
        Laboratorio lab = buscarLaboratorio(nomeLab);
        if (lab == null) {
            System.out.println("Laboratório não encontrado.");
            return; // Sai do menu
        }

        if (op == 1) {
            // Lista todas as estações e seus status
            for(int i = 0; i < lab.getEstacoes().size(); i++) {
                System.out.println(lab.getEstacoes().get(i));
            }
        } else if (op >= 2 && op <= 4) {
            System.out.print("Digite o ID da Estação (ex: 1): ");
            int idEstacao = sc.nextInt();
            sc.nextLine();
            Estacao est = lab.getEstacaoPorId(idEstacao);
            
            if (est != null) {
                if (op == 2) {
                    // 2. Registrar Manutenção
                    System.out.print("Descreva a manutenção: ");
                    String desc = sc.nextLine();
                    est.registrarManutencao(desc);
                } else if (op == 3) {
                    // 3. Bloquear Estação
                    est.setStatus(StatusEstacao.BLOQUEADA);
                    System.out.println("Estação " + est.getId() + " bloqueada.");
                } else if (op == 4) {
                    // Liberar (volta ao normal)
                    est.setStatus(StatusEstacao.DISPONIVEL);
                    System.out.println("Estação " + est.getId() + " liberada.");
                }
            } else {
                System.out.println("Estação não encontrada.");
            }
        }
    }
    
    // --- Métodos de Ajuda ---
    
    private void listarLaboratorios() {
        System.out.println("\n-- Lista de Laboratórios --");
        for(int i = 0; i < todosOsLaboratorios.size(); i++) {
            System.out.println(todosOsLaboratorios.get(i));
        }
    }

    private Laboratorio buscarLaboratorio(String nome) {
        for(int i = 0; i < todosOsLaboratorios.size(); i++) {
            Laboratorio lab = todosOsLaboratorios.get(i);
            if (lab.getNome().equalsIgnoreCase(nome)) {
                return lab;
            }
        }
        return null;
    }
}
