package com.example.a2.service;

import com.example.a2.data.Laboratorio;
import com.example.a2.data.ReservaRepository;
import com.example.a2.data.Reserva;

import java.util.List;

public class ReservaService {

    private ReservaRepository repositorio;

    public ReservaService() {
        this.repositorio = ReservaRepository.getInstance();
    }

    public List<Laboratorio> getLaboratorios() {
        return repositorio.getLaboratorios();
    }

    public List<Reserva> buscarMinhasReservas(String alunoId) {
        return repositorio.getReservasPorAluno(alunoId);
    }

    public Reserva getReservaById(String idReserva) {
        return repositorio.getReservaById(idReserva);
    }

    public synchronized boolean FazerReserva(Reserva novaReserva) {
        if (existeConflito(novaReserva)) {
            return false;
        }
        repositorio.addReserva(novaReserva);
        return true;
    }

    public void cancelarReserva(String idReserva) {
        repositorio.removerReserva(idReserva);
    }

    public synchronized boolean EditarReserva(String idReserva, String data, int minutoInicio, int minutoFim, String descricao, String labId) {
        Reserva reservaOriginal = repositorio.getReservaById(idReserva);
        if (reservaOriginal == null) {
            return false;
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

        if (existeConflito(reservaEditadaCheck)) {
            return false;
        }

        repositorio.updateReserva(idReserva, data, minutoInicio, minutoFim, descricao, labId);
        return true;
    }

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
