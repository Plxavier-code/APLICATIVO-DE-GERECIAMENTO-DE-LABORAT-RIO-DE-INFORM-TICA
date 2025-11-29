package com.example.a2.service;

import com.example.a2.data.Reserva;
import com.example.a2.data.ReservaRepository;
import java.util.List;

public class ReservaService {

    private ReservaRepository repository;

    public ReservaService() {
        this.repository = ReservaRepository.getInstance();
    }

    public boolean fazerReserva(Reserva novaReserva) {
        if (existeConflito(novaReserva)) {
            return false;
        }
        repository.adicionarReserva(novaReserva);
        return true;
    }

    public List<Reserva> buscarMinhasReservas(String usuarioId) {
        return repository.getReservasPorUsuario(usuarioId);
    }

    public void cancelarReserva(String idReserva) {
        repository.removerReserva(idReserva);
    }

    private boolean existeConflito(Reserva nova) {
        List<Reserva> existentes = repository.getReservas();
        String idLabPaiNovo = extrairLabPai(nova.getLaboratorioId());

        for (Reserva existente : existentes) {
            // 1. Verifica Data
            if (!existente.getData().equals(nova.getData())) continue;

            // 2. Verifica Horário (Interseção)
            boolean choqueHorario = (nova.getMinutoInicio() < existente.getMinutoFim()) &&
                    (nova.getMinutoFim() > existente.getMinutoInicio());

            if (choqueHorario) {
                // 3. Verifica Local
                String idLabPaiExistente = extrairLabPai(existente.getLaboratorioId());

                // Se estão no mesmo espaço físico (Laboratório)
                if (idLabPaiNovo.equals(idLabPaiExistente)) {
                    // Regra: Se um deles é reserva do laboratório INTEIRO, bloqueia tudo
                    boolean novaEhLabInteiro = !nova.getLaboratorioId().contains("est_");
                    boolean existenteEhLabInteiro = !existente.getLaboratorioId().contains("est_");

                    if (novaEhLabInteiro || existenteEhLabInteiro) {
                        return true; // Conflito: Lab inteiro vs Qualquer coisa
                    }

                    // Se ambos são estações, só conflita se for a MESMA estação
                    if (nova.getLaboratorioId().equals(existente.getLaboratorioId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Helper: Se for "est_1_lab_h401", retorna "lab_h401". Se for "lab_h401", retorna "lab_h401".
    private String extrairLabPai(String id) {
        if (id.startsWith("est_")) {
            // Lógica simples: procura o último trecho após o último underline se seguir o padrão
            // Ou, para o projeto atual, verificamos se contém o ID do lab
            if (id.contains("lab_h401")) return "lab_h401";
            if (id.contains("lab_h402")) return "lab_h402";
            if (id.contains("lab_h403")) return "lab_h403";
            if (id.contains("lab_h404")) return "lab_h404";
            if (id.contains("lab_h406")) return "lab_h406";
            if (id.contains("lab_h407")) return "lab_h407";
        }
        return id;
    }
}