package com.projeto.sistema_fila.dao.impl;

import com.projeto.sistema_fila.model.Reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InMemoryReservationDao {

    private final List<Reservation> reservations = new ArrayList<>();

    public void save(Reservation reservation) {
        reservations.add(reservation);
    }

    // substitui os métodos inexistentes getLab() e overlapsWith()
    public Reservation findActiveReservation(String labId, Date start, Date end) {

        for (Reservation r : reservations) {

            boolean sameLab = r.getLabId().equals(labId);

            // comparação manual, já compatível com API 24 e Java 11
            boolean overlaps =
                    r.getStart().before(end) &&
                            r.getEnd().after(start);

            if (sameLab && overlaps) {
                return r;
            }
        }

        return null;
    }
}
