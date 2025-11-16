package com.projeto.sistema_fila.model;


import java.util.Date;

public class QueueEntry {

    private String userId;
    private String labId;
    private Date arrivalTime;

    public QueueEntry(String userId, String labId, Date arrivalTime) {
        this.userId = userId;
        this.labId = labId;
        this.arrivalTime = arrivalTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getLabId() {      // ✔️ ESTE É O MÉTODO CORRETO
        return labId;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }
}




