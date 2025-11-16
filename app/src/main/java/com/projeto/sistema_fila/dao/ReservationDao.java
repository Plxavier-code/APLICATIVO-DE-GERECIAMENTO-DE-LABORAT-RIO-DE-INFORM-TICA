package com.projeto.sistema_fila.dao;

import com.projeto.sistema_fila.model.Reservation;

import com.projeto.sistema_fila.model.Reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationDao {

    private final List<Reservation> database = new ArrayList<>();

    public void save(Reservation reservation) {
        database.add(reservation);
    }

    // compatível com Java 11
    public Reservation findActiveReservation(String labId, Date start, Date end) {

        for (Reservation r : database) {

            boolean sameLab = r.getLabId().equals(labId);

            boolean overlap =
                    r.getStart().before(end) &&
                            r.getEnd().after(start);

            if (sameLab && overlap) {
                return r;
            }
        }

        return null;
    }
}



