package com.farhan.staradmin.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Algorithme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy = "algorithme", cascade = CascadeType.ALL)
    private List<Key> keys;

    public Algorithme(String name) {
        this.name = name;
    }

    public Algorithme() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
