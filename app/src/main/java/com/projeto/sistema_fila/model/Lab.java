package com.projeto.sistema_fila.model;

import java.util.Objects;

public class Lab {
    private final String id;
    private final String name;

    public Lab(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lab)) return false;
        Lab lab = (Lab) o;
        return id.equals(lab.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() { return "Lab{" + id + ", " + name + "}"; }
}
