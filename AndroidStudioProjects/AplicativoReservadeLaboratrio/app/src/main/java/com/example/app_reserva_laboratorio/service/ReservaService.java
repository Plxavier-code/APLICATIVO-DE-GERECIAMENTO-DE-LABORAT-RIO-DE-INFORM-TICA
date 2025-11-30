package com.example.app_reserva_laboratorio.service;

import android.content.Context;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.ReservaRepository;
import com.example.app_reserva_laboratorio.data.Reserva;

import java.util.List;

public class ReservaService {

    private ReservaRepository repositorio;
    private NotificationService notificationService;

    public ReservaService(Context context) {
        this.repositorio = ReservaRepository.getInstance();
        this.notificationService = new NotificationService(context);
    }

    public List<Laboratorio> getLaboratorios() {
        return repositorio.getLaboratorios();
    }

    public List<Reserva> getMinhasReservas(String alunoId) {
        return repositorio.getReservasPorAluno(alunoId);
    }

    public Reserva getReservaById(String idReserva) {
        return repositorio.getReservaById(idReserva);
    }

    /**
     * Método principal para salvar uma reserva. Agora é sincronizado.
     * Garante que apenas uma thread possa executar este método por vez, resolvendo a condição de corrida.
     *
     * @param novaReserva O objeto Reserva criado com os dados da tela.
     * @return true se a reserva foi salva, false se houve conflito.
     */
    public synchronized boolean FazerReserva(Reserva novaReserva) {
        // Validar conflitos
        if (existeConflito(novaReserva)) {
            // Conflito encontrado. A reserva falhou.
            return false;
        }

        // Se não tem conflito, salvar no repositório.
        repositorio.addReserva(novaReserva);
        notificationService.notificarProfessor(novaReserva);
        notificationService.notificarAlunoConfirmacao(novaReserva);
        return true;
    }

    /**
     * Cancela uma reserva.
     */
    public void cancelarReserva(String idReserva) {
        Reserva reserva = repositorio.getReservaById(idReserva);
        if (reserva != null) {
            repositorio.removerReserva(idReserva);
            notificationService.notificarAlunoCancelamento(reserva);
        }
    }

    /**
     * Método para editar uma reserva. Também deve ser sincronizado para evitar conflitos de edição.
     */
    public synchronized boolean EditarReserva(String idReserva, String data, int minutoInicio, int minutoFim, String descricao, String labId) {
        Reserva reservaOriginal = repositorio.getReservaById(idReserva);
        if (reservaOriginal == null) {
            return false; // Reserva não existe
        }

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
        Reserva reservaAtualizada = repositorio.getReservaById(idReserva);
        if(reservaAtualizada != null) {
            notificationService.notificarProfessor(reservaAtualizada);
            notificationService.notificarAlunoConfirmacao(reservaAtualizada);
        }
        return true;
    }


    // Método Privado de Validação

    private boolean existeConflito(Reserva reservaParaVerificar) {
        List<Reserva> reservasExistentes = repositorio.getReservas();

        Laboratorio labObjParaVerificar = repositorio.getLaboratorioById(reservaParaVerificar.getLaboratorioId());
        if (labObjParaVerificar == null) return true;
        String labPaiParaVerificar = getLabPai(labObjParaVerificar.getNome());
        boolean ehReservaDeLabInteiro = !labObjParaVerificar.getNome().contains("Estação");

        for (Reserva existente : reservasExistentes) {
            if (existente.getIdReserva().equals(reservaParaVerificar.getIdReserva())) {
                continue;
            }
            if (!existente.getData().equals(reservaParaVerificar.getData())) {
                continue;
            }

            Laboratorio labObjExistente = repositorio.getLaboratorioById(existente.getLaboratorioId());
            if (labObjExistente == null) continue;
            String labPaiExistente = getLabPai(labObjExistente.getNome());
            boolean existenteEhLabInteiro = !labObjExistente.getNome().contains("Estação");

            if (!labPaiParaVerificar.equals(labPaiExistente)) {
                continue;
            }

            boolean conflitoDeHorario =
                    (reservaParaVerificar.getMinutoInicio() < existente.getMinutoFim()) &&
                            (reservaParaVerificar.getMinutoFim() > existente.getMinutoInicio());

            if (!conflitoDeHorario) {
                continue;
            }

            if (ehReservaDeLabInteiro || existenteEhLabInteiro) {
                return true;
            }

            if (reservaParaVerificar.getLaboratorioId().equals(existente.getLaboratorioId())) {
                return true;
            }
        }

        return false;
    }

    private String getLabPai(String nomeCompleto) {
        if (nomeCompleto.startsWith("Estação")) {
            int index = nomeCompleto.indexOf(" - ");
            if (index != -1) {
                return nomeCompleto.substring(index + 3);
            }
        }
        return nomeCompleto;
    }
}
