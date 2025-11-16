package com.projeto.sistema_fila.dao.impl;

import com.projeto.sistema_fila.dao.LabDao;
import com.projeto.sistema_fila.model.Lab;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryLabDao implements LabDao {
    private final Map<String, Lab> map = new ConcurrentHashMap<>();

    @Override
    public void save(Lab lab) { map.put(lab.getId(), lab); }

    @Override
    public Optional<Lab> findById(String labId) { return Optional.ofNullable(map.get(labId)); }
}
