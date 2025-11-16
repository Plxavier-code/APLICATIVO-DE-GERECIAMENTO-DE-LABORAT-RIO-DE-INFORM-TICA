package com.projeto.sistema_fila.service;

import com.projeto.sistema_fila.dao.ReservationDao;
import com.projeto.sistema_fila.dao.QueueDao;
import com.projeto.sistema_fila.model.Reservation;
import com.projeto.sistema_fila.model.QueueEntry;

import java.util.Date;

public class ReservationService {

    private final ReservationDao reservationDao;
    private final QueueDao queueDao;

    public ReservationService(ReservationDao reservationDao, QueueDao queueDao) {
        this.reservationDao = reservationDao;
        this.queueDao = queueDao;
    }

    public boolean reserve(String userId, String labId, Date start, Date end) {

        // valida intervalo
        if (start.compareTo(end) >= 0) {
            return false;
        }

        Reservation existing = reservationDao.findActiveReservation(labId, start, end);

        if (existing == null) {
            Reservation r = new Reservation(userId, labId, start, end);
            reservationDao.save(r);
            return true;
        } else {
            // coloca na fila FIFO automaticamente
            QueueEntry entry = new QueueEntry(userId, labId, new Date());
            queueDao.enqueue(entry);
            return false;
        }
    }
}



