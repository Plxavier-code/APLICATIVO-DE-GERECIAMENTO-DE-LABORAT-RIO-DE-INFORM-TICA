spackage com.example.app_reserva_laboratorio.service;

import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.data.Reserva;

import java.util.List;

/**
 * Serviço (Service) - Implementa as "Regras de Negócio".
 * É o cérebro que decide se uma reserva pode ou não ser feita.
 */
public class ReservaService {

    private ReservaRepository repositorio;

    // Construtor
    public ReservaService() {
        // Pega a instância única do nosso "banco de dados"
        this.repositorio = ReservaRepository.getInstance();
    }

    // Métodos de Leitura (READ)

    /**
     * Busca a lista completa de laboratórios.
     * (Usado pela tela ListaLabsActivity)
     *
     * @return Lista de todos os Laboratorios.
     */
    public List<Laboratorio> getLaboratorios() {
        return repositorio.getLaboratorios();
    }

    /**
     * Busca as reservas de um aluno específico.
     * (Usado pela tela MinhasReservasActivity)
     *
     * @param alunoId O ID do aluno logado.
     * @return Lista de Reservas daquele aluno.
     */
    public List<Reserva> getMinhasReservas(String alunoId) {
        return repositorio.getReservasPorAluno(alunoId);
    }

    /**
     * Busca uma única reserva pelo seu ID.
     * (Usado pelas telas EditarReservaActivity e EditarEstacaoActivity)
     *
     * @param idReserva O ID da reserva.
     * @return O objeto Reserva.
     */
    public Reserva getReservaById(String idReserva) {
        return repositorio.getReservaById(idReserva);
    }


    // Métodos de Escrita (CREATE, UPDATE, DELETE)

    /**
     * Método principal para a tela "NovaReservaActivity".
     * Tenta salvar uma reserva, mas antes valida os conflitos.
     *
     * @param novaReserva O objeto Reserva criado com os dados da tela.
     * @return true se a reserva foi salva, false se houve conflito.
     */
    public boolean FazerReserva(Reserva novaReserva) {

        // Validar conflitos
        if (existeConflito(novaReserva)) {
            // Conflito encontrado. A reserva falhou.
            return false;
        }

        // Se não tem conflito, salvar no repositório.
        repositorio.addReserva(novaReserva);
        return true;
    }

    /**
     * Cancela uma reserva.
     */
    public void cancelarReserva(String idReserva) {
        repositorio.removerReserva(idReserva);
    }

    /**
     * Método para a tela "EditarReservaActivity".
     * Tenta editar uma reserva, mas antes valida os conflitos.
     */
    public boolean EditarReserva(String idReserva, String data, int minutoInicio, int minutoFim, String descricao, String labId) {

        // Pega os dados originais para simular a edição
        Reserva reservaOriginal = repositorio.getReservaById(idReserva);
        if (reservaOriginal == null) {
            return false; // Reserva não existe
        }

        // Cria um "clone" temporário da reserva com os novos dados
        Reserva reservaEditadaCheck = new Reserva(
                idReserva,
                labId,
                data,
                minutoInicio,
                minutoFim,
                descricao,
                reservaOriginal.getAlunoId()
        );

        // Validar conflitos
        if (existeConflito(reservaEditadaCheck)) {
            return false; // Conflito encontrado!
        }

        //Se não tem conflito, aplicar a edição no repositório.
        repositorio.updateReserva(idReserva, data, minutoInicio, minutoFim, descricao, labId);
        return true;
    }


    // Método Privado de Validação

    /**
     * Lógica principal
     * Verifica se uma reserva (nova ou editada) conflita com as existentes.
     *
     * @param reservaParaVerificar A reserva que queremos salvar.
     * @return true se há conflito, false se não há conflito.
     */
    private boolean existeConflito(Reserva reservaParaVerificar) {
        // Pega a lista de todas as reservas que já existem
        List<Reserva> reservasExistentes = repositorio.getReservas();

        // Pega o "Lab Pai" da reserva que queremos fazer
        Laboratorio labObjParaVerificar = repositorio.getLaboratorioById(reservaParaVerificar.getLaboratorioId());
        if (labObjParaVerificar == null) return true; // Segurança: Se o lab não existe, bloqueia
        String labPaiParaVerificar = getLabPai(labObjParaVerificar.getNome());
        boolean ehReservaDeLabInteiro = !labObjParaVerificar.getNome().contains("Estação");

        for (Reserva existente : reservasExistentes) {
            // Pula a verificação se for a mesma reserva
            if (existente.getIdReserva().equals(reservaParaVerificar.getIdReserva())) {
                continue;
            }
            // Pula se for em um dia diferente
            if (!existente.getData().equals(reservaParaVerificar.getData())) {
                continue;
            }

            // Pega o "Lab Pai" da reserva existente
            Laboratorio labObjExistente = repositorio.getLaboratorioById(existente.getLaboratorioId());
            if (labObjExistente == null) continue; // Pula se a reserva antiga for inválida
            String labPaiExistente = getLabPai(labObjExistente.getNome());
            boolean existenteEhLabInteiro = !labObjExistente.getNome().contains("Estação");

            // Checa se são do mesmo "Lab Pai"
            if (!labPaiParaVerificar.equals(labPaiExistente)) {
                continue; // Laboratórios principais diferentes, sem conflito.
            }

            // Se chegou aqui, é no mesmo dia e no mesmo LAB PAI
            // Checa a sobreposição de horário
            boolean conflitoDeHorario =
                    (reservaParaVerificar.getMinutoInicio() < existente.getMinutoFim()) &&
                            (reservaParaVerificar.getMinutoFim() > existente.getMinutoInicio());

            if (!conflitoDeHorario) {
                continue; // Horários diferentes, sem conflito.
            }

            // Se chegou aqui, é mesmo dia, mesmo LAB PAI e mesmo horário.
            // Se uma das reservas for do LAB inteiro, há conflito.
            if (ehReservaDeLabInteiro || existenteEhLabInteiro) {
                return true; // conflito
            }

            // Se nenhuma é do lab inteiro, são duas estações.
            // Só há conflito se forem a mesma estação.
            if (reservaParaVerificar.getLaboratorioId().equals(existente.getLaboratorioId())) {
                return true; // conflito
            }

            // Se são estações diferentes (est_12 vs est_10), o loop continua (sem conflito).
        }

        // Se o loop terminou e não achou nada, o horário está livre.
        return false;
    }

    /**
     * Método "Helper" para encontrar o nome do Laboratório "Pai".
     * Ex: "Estação 12 - LAB - H403" -> "LAB - H403"
     * Ex: "LAB - H401" -> "LAB - H401"
     */
    private String getLabPai(String nomeCompleto) {
        // Se for uma estação, queremos extrair o nome do Lab Pai
        if (nomeCompleto.startsWith("Estação")) {
            // Procura o primeiro " - "
            int index = nomeCompleto.indexOf(" - ");
            if (index != -1) {
                // Retorna tudo o que vem depois de "Estação X - "
                return nomeCompleto.substring(index + 3);
            }
        }
        // Se não for uma estação, ele já é o Lab Pai, então retorna o nome completo.
        return nomeCompleto;
    }
}