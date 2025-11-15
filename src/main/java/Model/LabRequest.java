package Model;

public class LabRequest {

    private final String professorName;
    private final String labName; // exemplo: "Lab 101", "Lab 202"
    private final long arrivalOrder;

    public LabRequest(String professorName, String labName, long arrivalOrder) {
        this.professorName = professorName;
        this.labName = labName;
        this.arrivalOrder = arrivalOrder;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getLabName() {
        return labName;
    }

    public long getArrivalOrder() {
        return arrivalOrder;
    }
}

