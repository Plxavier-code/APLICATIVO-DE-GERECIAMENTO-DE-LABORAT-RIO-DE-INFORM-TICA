import java.util.*;

public class Main {

    static Map<Integer, String> ocupados = new HashMap<>();
    static Map<Integer, Queue<String>> filas = new HashMap<>();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Sistema de Laboratórios ---");
            System.out.println("1 - Solicitar laboratório");
            System.out.println("2 - Liberar laboratório");
            System.out.println("3 - Sair");
            System.out.print("Escolha: ");

            String entrada = sc.nextLine();

            int opcao;
            try { opcao = Integer.parseInt(entrada); }
            catch (Exception e) { System.out.println("Opção inválida!"); continue; }

            if (opcao == 1) {
                System.out.print("Professor: ");
                String professor = sc.nextLine();

                System.out.print("Laboratório (número): ");
                int lab = Integer.parseInt(sc.nextLine());

                solicitarLaboratorio(lab, professor);
            }

            else if (opcao == 2) {
                System.out.print("Laboratório a liberar: ");
                int lab = Integer.parseInt(sc.nextLine());

                liberarLaboratorio(lab);
            }

            else if (opcao == 3) break;

            else System.out.println("Opção inválida!");
        }
    }

    static void solicitarLaboratorio(int lab, String professor) {

        if (!ocupados.containsKey(lab)) {
            ocupados.put(lab, professor);
            System.out.println("Laboratório " + lab + " reservado com sucesso!");
        } else {
            System.out.println("Laboratório ocupado! Você foi colocado na fila.");

            filas.putIfAbsent(lab, new LinkedList<>());
            filas.get(lab).add(professor);
        }
    }

    static void liberarLaboratorio(int lab) {

        if (filas.containsKey(lab) && !filas.get(lab).isEmpty()) {
            String proximo = filas.get(lab).poll();
            ocupados.put(lab, proximo);

            System.out.println("Laboratório " + lab + " realocado automaticamente!");
            System.out.println("Próximo da fila: " + proximo);
        } else {
            ocupados.remove(lab);
            System.out.println("Laboratório " + lab + " agora está livre!");
        }
    }
}
