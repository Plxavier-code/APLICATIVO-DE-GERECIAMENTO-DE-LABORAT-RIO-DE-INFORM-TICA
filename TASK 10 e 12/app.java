public class app {
    public static void main(String[] args) throws InterruptedException {
        SessionService service = new SessionService();

        Usuario aluno = new Usuario("1", "Higor", "aluno");
        Usuario professor = new Usuario("2", "Marcos", "professor");
        Usuario convidado = new Usuario("3", "Laura", "convidado");

        System.out.println("-- 1. Início da primeira sessão (Estacao-01)");
        service.iniciarSessao(aluno, "Estacao-01");

        System.out.println("\n-- 2. Tentativa de iniciar sessão ocupada (Entra na Fila)");
        service.iniciarSessao(professor, "Estacao-01"); 

        System.out.println("\n-- 3. Outro usuário tenta entrar na mesma estação (Entra na Fila)");
        service.iniciarSessao(convidado, "Estacao-01");

        System.out.println("\n-- 4. Início em outra estação");
        service.iniciarSessao(aluno, "Estacao-02");

        System.out.println("\n-- 5. Tentativa de entrar em fila de sessão ativa");
        service.iniciarSessao(aluno, "Estacao-03");

        Thread.sleep(2000);

        System.out.println("\n-- 6. Finaliza a primeira sessão (Libera Estacao-01)");
        service.finalizarSessao(aluno);

        System.out.println("\n-- 7. Professor inicia a sessão (É a vez dele)");
        service.iniciarSessao(professor, "Estacao-01");

        System.out.println("\n-- 8. Laura tenta iniciar sessão em outra estação");
        service.iniciarSessao(convidado, "Estacao-02");

        Thread.sleep(1000);

        System.out.println("\n-- 9. Professor finaliza a sessão (Libera Estacao-01)");
        service.finalizarSessao(professor);

        System.out.println("\n-- 10. Convidado inicia a sessão (É a vez dela)");
        service.iniciarSessao(convidado, "Estacao-01");

        System.out.println("\n-- 11. Convidado sai da fila (por ter sessão em outro lugar)");
        service.sairDaFila(convidado);

        System.out.println("\n-- 12. Listar Histórico");
        service.listarHistorico();
    }
}