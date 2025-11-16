package DAO;

import Model.LabRequest;
import java.util.LinkedList;
import java.util.Queue;

public class LabRequestDAO {

    private final Queue<LabRequest> queue = new LinkedList<>();

    public void insert(LabRequest request) {
        queue.add(request);
    }

    public LabRequest poll() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}

