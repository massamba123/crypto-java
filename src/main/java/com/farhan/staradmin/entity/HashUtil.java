package com.farhan.staradmin.entity;

public class HashUtil {
    private String algorithme;
    private String message;

    public HashUtil(String algorithme, String message) {
        this.algorithme = algorithme;
        this.message = message;
    }

    public String getAlgorithme() {
        return algorithme;
    }

    public void setAlgorithme(String algorithme) {
        this.algorithme = algorithme;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
