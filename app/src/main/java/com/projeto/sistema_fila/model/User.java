package com.projeto.sistema_fila.model;

import java.util.Objects;

public class User {
    private final String id;
    private final String name;

    private final String role;
    private final int priority;

    public User(String id, String name, String role, int priority) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.priority = priority;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public int getPriority() { return priority; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "User{" + id + ", " + name + ", role=" + role + ", prio=" + priority + '}';
    }
}

