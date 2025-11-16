package com.projeto.sistema_fila.service;

import com.projeto.sistema_fila.dao.impl.InMemoryLabDao;
import com.projeto.sistema_fila.labqueue.dao.impl.InMemoryQueueDao;
import com.projeto.sistema_fila.labqueue.dao.impl.InMemoryReservationDao;
import com.projeto.sistema_fila.labqueue.model.*;
import com.projeto.sistema_fila.labqueue.service.ReservationService;
import com.projeto.sistema_fila.labqueue.service.ReservationService.ReservationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceTest {

    private InMemoryReservationDao reservationDao;
    private InMemoryQueueDao queueDao;
    private InMemoryLabDao labDao;
    private ReservationService reservationService;
    private Lab labA;

    @BeforeEach
    public void setup() {
        reservationDao = new InMemoryReservationDao();
        queueDao = new InMemoryQueueDao();
        labDao = new InMemoryLabDao();
        reservationService = new ReservationService(reservationDao, queueDao, labDao);

        labA = new Lab("lab-a", "Laboratório A");
        labDao.save(labA);
    }

    @Test
    public void testAutomaticEnqueueWhenAlreadyReserved() {
        User u1 = new User("u1", "Alice", "student", 5);
        User u2 = new User("u2", "Bob", "student", 5);

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        // u1 reserva com sucesso
        ReservationResult r1 = reservationService.reserve(labA.getId(), u1, start, end);
        assertEquals(ReservationService.ReservationResultType.SUCCESS, r1.getType());

        // u2 tenta reservar mesmo intervalo -> deve ser enfileirado
        ReservationResult r2 = reservationService.reserve(labA.getId(), u2, start, end);
        assertEquals(ReservationService.ReservationResultType.QUEUED, r2.getType());

        // fila do laboratório deve conter u2
        List<com.example.labqueue.model.QueueEntry> q = queueDao.getQueueForLab(labA.getId());
        assertEquals(1, q.size());
        assertEquals("u2", q.get(0).getUser().getId());
    }

    @Test
    public void testPriorityThenFIFOOrdering() throws InterruptedException {
        // três usuários com mesmas prioridades -> FIFO por ordem de inserção
        User u1 = new User("u1", "U1", "student", 10);
        User u2 = new User("u2", "U2", "student", 10);
        User u3 = new User("u3", "U3", "student", 10);

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(2);

        // reserve by u1
        ReservationResult r1 = reservationService.reserve(labA.getId(), u1, start, end);
        assertEquals(ReservationService.ReservationResultType.SUCCESS, r1.getType());

        // attempt with u2, u3 -> both enqueued in that order
        ReservationResult r2 = reservationService.reserve(labA.getId(), u2, start, end);
        assertEquals(ReservationService.ReservationResultType.QUEUED, r2.getType());

        // small pause to ensure seq ordering is consistent (QueueEntry uses seq anyway)
        Thread.sleep(5);

        ReservationResult r3 = reservationService.reserve(labA.getId(), u3, start, end);
        assertEquals(ReservationService.ReservationResultType.QUEUED, r3.getType());

        List<com.example.labqueue.model.QueueEntry> q = queueDao.getQueueForLab(labA.getId());
        assertEquals(2, q.size());
        // order should be u2 then u3
        assertEquals("u2", q.get(0).getUser().getId());
        assertEquals("u3", q.get(1).getUser().getId());
    }

    @Test
    public void testDequeueOnCancelAllocatesNext() {
        User u1 = new User("u1", "Alice", "student", 10);
        User u2 = new User("u2", "Bob", "student", 10);

        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusHours(2);

        // u1 reserves
        var res1 = reservationService.reserve(labA.getId(), u1, start, end);
        assertEquals(ReservationService.ReservationResultType.SUCCESS, res1.getType());
        com.example.labqueue.model.Reservation r1 = res1.getReservation();

        // u2 tries and is queued
        var res2 = reservationService.reserve(labA.getId(), u2, start, end);
        assertEquals(ReservationService.ReservationResultType.QUEUED, res2.getType());

        // cancel r1 -> should allocate to u2
        boolean cancelled = reservationService.cancelReservation(r1);
        assertTrue(cancelled);

        var all = reservationDao.findAll();
        assertEquals(1, all.size());
        assertEquals("u2", all.get(0).getUser().getId());
    }
}

