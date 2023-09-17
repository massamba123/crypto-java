package com.farhan.staradmin.entity;

import com.farhan.staradmin.service.ChiffrementUtil;
import com.farhan.staradmin.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.File;
import java.util.*;

@Entity
public class Chiffrement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String type;
    private String algorithme;
    private int size;
    @Column(nullable = false)
    private String mode;
    private String keyPath;
    private String fileInPath;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(nullable = true)
    private String message;


    public List<String> getTypes(){
        return Arrays.asList("symetrique","asymetrique","partage cle");
    }
    public  List<String> getAlgoTypes(){
        switch (this.getType()) {
            case "symetrique":
                return Arrays.asList("AES", "DES");
            case "asymetrique":
                return Arrays.asList("RSA", "DSA");
            case "partage cle":
                return Collections.singletonList("Diffie-Hellman");
        }
        return null;
    }
    public List<String> getListeModeChiffrement(){
        return Arrays.asList("Chiffrement","Dechiffrement");
    }
    public Map<String, List<Integer>> getAllSizeAlgorithm() {
        Map<String, List<Integer>> types = new HashMap<>();

        // AES key sizes
        types.put("AES", Arrays.asList(128, 192, 256));
        types.put("DES", Collections.singletonList(56));
        types.put("DESede", Arrays.asList(112, 168)); // Separate values with commas
        types.put("Blowfish", Arrays.asList(32, 448)); // Separate values with commas
        types.put("IDEA", Collections.singletonList(128));
        types.put("Camellia", Arrays.asList(128, 192, 256));
        types.put("SEED", Collections.singletonList(128));
        types.put("Serpent", Arrays.asList(128, 192, 256));
        types.put("RC4", Arrays.asList(40, 128, 256, 2048)); // Separate values with commas
        types.put("ChaCha20", Collections.singletonList(256));

        // Algorithmes de chiffrement asymétrique
        types.put("RSA", Arrays.asList(1024, 2048, 3072, 4096));
        types.put("ECDSA", Arrays.asList(256, 384,521)); // Adjust as needed
        types.put("ElGamal", Arrays.asList(1024, 2048, 3072, 4096));


        // DSA key sizes
        return types;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlgorithme() {
        return algorithme;
    }

    public void setAlgorithme(String algorithme) {
        this.algorithme = algorithme;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getFileInPath() {
        return fileInPath;
    }

    public void setFileInPath(String fileInPath) {
        this.fileInPath = fileInPath;
    }
}
