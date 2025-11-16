package com.projeto.sistema_fila.model;

import java.util.Date;

public class Reservation {

    private final String userId;
    private final String labId;
    private final Date start;
    private final Date end;

    public Reservation(String userId, String labId, Date start, Date end) {
        this.userId = userId;
        this.labId = labId;
        this.start = start;
        this.end = end;
    }

    public String getUserId() { return userId; }
    public String getLabId() { return labId; }
    public Date getStart() { return start; }
    public Date getEnd() { return end; }
}


