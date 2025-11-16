package com.projeto.sistema_fila.dao;

import com.projeto.sistema_fila.model.Lab;
import java.util.Optional;

public interface LabDao {
    void save(Lab lab);
    Optional<Lab> findById(String labId);
}

