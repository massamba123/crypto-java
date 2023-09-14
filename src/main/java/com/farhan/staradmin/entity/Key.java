package com.farhan.staradmin.entity;

import javax.persistence.*;

@Entity
public class Key {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String size;
    @ManyToOne
    @JoinColumn(name = "algorithme_id")
    private Algorithme algorithme;

    public Key(String name, String size) {
        this.name = name;
        this.size = size;
    }

    public Key() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
