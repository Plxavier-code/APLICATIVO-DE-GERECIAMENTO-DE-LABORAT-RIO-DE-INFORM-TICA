package com.projeto.sistema_fila.dao.impl;

import com.projeto.sistema_fila.dao.QueueDao;
import com.projeto.sistema_fila.model.QueueEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InMemoryQueueDao implements QueueDao {

    private final List<QueueEntry> queue = new ArrayList<>();

    @Override
    public void enqueue(QueueEntry entry) {
        queue.add(entry);
    }

    @Override
    public QueueEntry dequeue(String labId) {
        for (Iterator<QueueEntry> it = queue.iterator(); it.hasNext(); ) {
            QueueEntry entry = it.next();
            if (entry.getLabId().equals(labId)) {  // <-- CORREÇÃO AQUI
                it.remove();
                return entry;
            }
        }
        return null;
    }

    @Override
    public List<QueueEntry> getQueueForLab(String labId) {
        List<QueueEntry> result = new ArrayList<>();
        for (QueueEntry entry : queue) {
            if (entry.getLabId().equals(labId)) {   // <-- CORREÇÃO AQUI
                result.add(entry);
            }
        }
        return result;
    }

    @Override
    public void removeEntriesByUserAndLab(String userId, String labId) {
        queue.removeIf(e ->
                e.getUserId().equals(userId) &&
                        e.getLabId().equals(labId)         // <-- CORREÇÃO AQUI
        );
    }
}

