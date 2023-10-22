package com.farhan.staradmin.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signature {
    private String algorithme;
    private String hash;
    private int size;

    public Signature(String algorithme, String hash, int size) {
        this.algorithme = algorithme;
        this.hash = hash;
        this.size = size;
    }
    public Signature(){}
    public List<String> getAlgoSignature(){
        return Arrays.asList("RSA","DSA","ECDSA","EdDSA");
    }
    public Map<String, List<String>> getAllAlgoSignature(){
        Map<String, List<String>> options = new HashMap<>();
        options.put("RSA", Arrays.asList("SHA256withRSA","SHA512withRSA"));
        options.put("DSA", Arrays.asList("SHA256withDSA","SHA512withDSA"));
        options.put("ECDSA", Arrays.asList("SHA256withECDSA","SHA256withECDSA"));
        options.put("EdDSA", Arrays.asList("Ed25519","Ed25519"));
        return options;
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
