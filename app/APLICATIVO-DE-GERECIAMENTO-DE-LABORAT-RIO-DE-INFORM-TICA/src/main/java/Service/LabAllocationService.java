package Service;

import DAO.LabRequestDAO;
import Model.LabRequest;

public class LabAllocationService {

    private final LabRequestDAO dao;
    private long orderCounter = 0; // registra ordem FIFO

    public LabAllocationService(LabRequestDAO dao) {
        this.dao = dao;
    }

    // Professor solicita laboratório
    public void requestLab(String professorName, String labName) {
        LabRequest request = new LabRequest(professorName, labName, orderCounter++);
        dao.insert(request);
    }

    // Aloca o próximo da fila para usar a sala
    public LabRequest allocateNext() {
        return dao.poll();
    }
}

