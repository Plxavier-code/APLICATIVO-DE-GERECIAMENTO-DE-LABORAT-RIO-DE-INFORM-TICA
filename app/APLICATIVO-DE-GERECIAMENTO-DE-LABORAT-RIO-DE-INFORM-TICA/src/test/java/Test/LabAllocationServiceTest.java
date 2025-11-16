package Test;

import DAO.LabRequestDAO;
import Model.LabRequest;
import Service.LabAllocationService;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LabAllocationServiceTest {

    @Test
    public void testFifoOrder() {
        LabRequestDAO dao = new LabRequestDAO();
        LabAllocationService service = new LabAllocationService(dao);

        service.requestLab("Cláudio", "Lab 101");
        service.requestLab("Eliane", "Lab 202");
        service.requestLab("Leonardo", "Lab 401");

        LabRequest r1 = service.allocateNext();
        LabRequest r2 = service.allocateNext();
        LabRequest r3 = service.allocateNext();

        assertEquals("Cláudio", r1.getProfessorName());
        assertEquals("Eliane", r2.getProfessorName());
        assertEquals("Leonardo", r3.getProfessorName());
    }

    @Test
    public void testEmptyQueueReturnsNull() {
        LabAllocationService service = new LabAllocationService(new LabRequestDAO());
        assertNull(service.allocateNext());
    }

    @Test
    public void testArrivalOrderIncremented() {
        LabRequestDAO dao = new LabRequestDAO();
        LabAllocationService service = new LabAllocationService(dao);

        service.requestLab("Cláudio", "Lab 101");
        service.requestLab("Eliane", "Lab 202");

        LabRequest r1 = dao.poll();
        LabRequest r2 = dao.poll();

        assertTrue(r1.getArrivalOrder() < r2.getArrivalOrder());
    }
}

