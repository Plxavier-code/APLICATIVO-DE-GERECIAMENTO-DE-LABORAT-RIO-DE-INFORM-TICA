package com.projeto.sistema_fila;

import com.projeto.sistema_fila.dao.impl.InMemoryQueueDao;
import com.projeto.sistema_fila.dao.ReservationDao;
import com.projeto.sistema_fila.model.Reservation;
import com.projeto.sistema_fila.model.QueueEntry;
import com.projeto.sistema_fila.service.ReservationService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {


        ReservationDao reservationDao = new ReservationDao();
        InMemoryQueueDao queueDao = new InMemoryQueueDao();


        ReservationService service = new ReservationService(reservationDao, queueDao);


        Date agora = new Date();
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.HOUR, 1);
        Date maisTarde = cal.getTime();


        System.out.println("--- SISTEMA DE FILA FIFO ---");


        boolean r1 = service.reserve("A", "LAB1", agora, maisTarde);
        System.out.println("A reservou? " + r1);  // true


        boolean r2 = service.reserve("B", "LAB1", agora, maisTarde);
        System.out.println("B reservou? " + r2);  // false


        boolean r3 = service.reserve("C", "LAB1", agora, maisTarde);
        System.out.println("C reservou? " + r3);  // false

        // Exibir quem está na fila
        List<QueueEntry> fila = queueDao.getQueueForLab("LAB1");

        System.out.println("\n--- FILA DO LAB1 ---");
        for (QueueEntry q : fila) {
            System.out.println("USER: " + q.getUserId() + " (chegou em " + q.getArrivalTime() + ")");
        }

    }
}

