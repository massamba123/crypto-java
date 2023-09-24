package com.farhan.staradmin.entity;

public class Signature {
    private String algorithme;
    private String hash;
    private int size;

    public Signature(String algorithme, String hash, int size) {
        this.algorithme = algorithme;
        this.hash = hash;
        this.size = size;
    }

    public String getAlgorithme() {
        return algorithme;
    }

    public void setAlgorithme(String algorithme) {
        this.algorithme = algorithme;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
