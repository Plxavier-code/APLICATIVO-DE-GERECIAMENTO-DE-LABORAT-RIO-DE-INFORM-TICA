package com.projeto.sistema_fila.dao;

import com.projeto.sistema_fila.model.QueueEntry;
import java.util.List;

public interface QueueDao {
    void enqueue(QueueEntry entry);
    QueueEntry dequeue(String labId);
    List<QueueEntry> getQueueForLab(String labId);
    void removeEntriesByUserAndLab(String userId, String labId);
}

